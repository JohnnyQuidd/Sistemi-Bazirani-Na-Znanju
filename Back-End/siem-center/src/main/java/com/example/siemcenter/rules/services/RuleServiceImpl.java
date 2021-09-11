package com.example.siemcenter.rules.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.repositories.AlarmRepository;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.rules.dtos.RuleDeviceDTO;
import com.example.siemcenter.rules.models.Rule;
import com.example.siemcenter.rules.repositories.RuleRepository;
import com.example.siemcenter.users.drools.UserTrait;
import com.example.siemcenter.users.models.User;
import com.example.siemcenter.users.repositories.UserRepository;
import com.example.siemcenter.util.DevicesForUser;
import org.drools.core.ClockType;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {
    private static Integer DEVICE_NUMBER = 1;
    private Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);
    private RuleRepository ruleRepository;
    private KieSession session;
    private DeviceRepository deviceRepository;
    private AlarmRepository alarmRepository;
    private UserRepository userRepository;
    private DevicesForUser devicesForUser;
    private KieHelper kieHelper;
    private List<String> deviceList;


    @Autowired
    public RuleServiceImpl(RuleRepository ruleRepository,
                           DeviceRepository deviceRepository,
                           AlarmRepository alarmRepository,
                           UserRepository userRepository,
                           DevicesForUser devicesForUser,
                           KieHelper kieHelper) {
        this.ruleRepository = ruleRepository;
        this.deviceRepository = deviceRepository;
        this.alarmRepository = alarmRepository;
        this.userRepository = userRepository;
        this.devicesForUser = devicesForUser;
        this.kieHelper = kieHelper;
        this.deviceList = new ArrayList<>();

        session = setUpSessionForStreamProcessingMode();

        // TODO: Load rules properly
        //loadRules();
        setGlobals();
    }


    private void setGlobals() {
        session.setGlobal("logger", logger);
        session.setGlobal("deviceRepository", deviceRepository);
        session.setGlobal("alarmRepository", alarmRepository);
        session.setGlobal("userRepository", userRepository);
        session.setGlobal("devicesForUser", devicesForUser);
        session.setGlobal("deviceNumber", DEVICE_NUMBER);
        session.setGlobal("deviceList", deviceList);
    }

    private KieSession setUpSessionForStreamProcessingMode() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.newKieClasspathContainer();

        KieSessionConfiguration ksconf = ks.newKieSessionConfiguration();
        ksconf.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));

        KieSession session = kc.newKieSession("session1", ksconf);
        return session;
    }

    private void loadRules() {
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        List<Rule> ruleList = ruleRepository.findAll();
        for(Rule rule : ruleList) {
            Resource r = ResourceFactory.newReaderResource(new StringReader(rule.getContent()));
            logger.info("Loading DRL...");
            logger.info(rule.getContent());
            knowledgeBuilder.add(r, ResourceType.DRL);
        }
        KieBase kieBase = knowledgeBuilder.newKieBase();
        session = kieBase.newKieSession();
    }

    @Override
    public void insertLog(Log log) {
        session.insert(log);
        session.fireAllRules();
    }

    @Override
    public void insertAlarm(Alarm alarm) {
        session.insert(alarm);
        session.fireAllRules();
    }

    @Override
    public List<Log> getLogs() {
        return logsFetchingBySession(session);
    }

    @Override
    public List<Log> getLogsBySession(KieSession kieSession) {
        return logsFetchingBySession(kieSession);
    }

    public List<Log> logsFetchingBySession(KieSession currentSession) {
        List<Log> logs = new LinkedList<>();
        QueryResults allLogs = currentSession.getQueryResults("fetchAllLogs");
        for (QueryResultsRow singleRow : allLogs) {
            logs.add((Log) singleRow.get("$allLogs"));
        }
        return logs;
    }

    @Override
    public List<Alarm> getAlarms() {
       return fetchAlarmsByCurrentSession(session);
    }

    @Override
    public List<Alarm> getAlarmsBySession(KieSession kieSession) {
        return fetchAlarmsByCurrentSession(kieSession);
    }

    private List<Alarm> fetchAlarmsByCurrentSession(KieSession currentSession) {
        List<Alarm> alarms = new LinkedList<>();
        QueryResults allAlarms = currentSession.getQueryResults("fetchAllAlarms");
        for (QueryResultsRow singleRow : allAlarms) {
            alarms.add((Alarm) singleRow.get("$allAlarms"));
        }
        return alarms;
    }

    @Override
    public List<Alarm> getAlarmForRule(String ruleName) {
        return getAlarmsForRuleAndCurrentSession(session, ruleName);
    }

    @Override
    public List<Alarm> getAlarmForRuleAndSession(String ruleName, KieSession kieSession) {
        return getAlarmsForRuleAndCurrentSession(kieSession, ruleName);
    }

    private List<Alarm> getAlarmsForRuleAndCurrentSession(KieSession currentSession, String ruleName) {
        List<Alarm> alarms = new LinkedList<>();
        QueryResults allAlarms = currentSession.getQueryResults("fetchAlarmsForRuleName", ruleName);
        for (QueryResultsRow singleRow : allAlarms) {
            alarms.add((Alarm) singleRow.get("$alarms"));
        }
        return alarms;
    }

    @Override
    public List<User> getUsersForSixOrMoreAlarms() {
        return fetchUsersForRule(23);
    }

    @Override
    public List<User> usersWithMultipleFailedLogins(int deviceNum) {
        DEVICE_NUMBER = deviceNum;
        return fetchUsersForRule(24);
    }

    @Override
    public List<User> getUsersForTenAlarmsInTenPastDays() {
        return fetchUsersForRule(25);
    }

    private List<User> fetchUsersForRule(int ruleNumber) {
        List<UserTrait> users = new LinkedList<>();
        QueryResults userResults = session.getQueryResults("fetchUsersForReportCreation");
        for (QueryResultsRow singleRow : userResults) {
            UserTrait trait = (UserTrait) singleRow.get("$user");
            if (trait.getRuleTriggered().equals("#" + ruleNumber)) {
                users.add(trait);
            }
        }

        return fromTraitToModel(users);
    }

    public List<Log> fetchLogsByRegex(String regex) {
        return fetchRegexLogsForSession(session, regex);
    }

    @Override
    public List<Log> fetchLogsByRegexAndSession(String regex, KieSession kieSession) {
        return fetchRegexLogsForSession(kieSession, regex);
    }

    private List<Log> fetchRegexLogsForSession(KieSession currentSession, String regex) {
        List<Log> logList = new LinkedList<>();
        QueryResults logResults = currentSession.getQueryResults("fetchLogsByRegex", regex);
        for (QueryResultsRow singleRow : logResults) {
            logList.add((Log) singleRow.get("$regexLogs"));
        }
        return logList;
    }

    @Override
    public List<Alarm> fetchAlarmsByRegex(String regex) {
        return fetchRegexAlarmsForSession(session, regex);
    }

    @Override
    public List<Alarm> fetchAlarmsByRegexAndSession(String regex, KieSession kieSession) {
        return fetchRegexAlarmsForSession(kieSession, regex);
    }

    private List<Alarm> fetchRegexAlarmsForSession(KieSession currentSession, String regex) {
        List<Alarm> alarmList = new LinkedList<>();
        QueryResults logResults = currentSession.getQueryResults("fetchAlarmsByRegex", regex);
        for (QueryResultsRow singleRow : logResults) {
            alarmList.add((Alarm) singleRow.get("$regexAlarms"));
        }
        return alarmList;
    }

    @Override
    public void createNewRuleFromUserDeviceDTO(RuleDeviceDTO dto) {
        InputStream template = RuleServiceImpl.class.getResourceAsStream("/templates/DeviceBlacklistTemplate.drt");
        ObjectDataCompiler converter = new ObjectDataCompiler();

        List<RuleDeviceDTO> data = Arrays.asList(dto);
        String drl = converter.compile(data, template);
        logger.info("Rule created");
        logger.info(drl);

        Rule rule = Rule.builder()
                .content(drl)
                .build();

        ruleRepository.save(rule);
        // Todo: Load rules after insertion
        //loadRules();
    }

    private List<User> fromTraitToModel(List<UserTrait> userTraits) {
        return userTraits.stream().map(trait ->
                User.builder()
                        .id(trait.getId())
                        .username(trait.getUsername())
                        .password(trait.getPassword())
                        .lastTimeUserWasActive(trait.getLastTimeUserWasActive())
                        .riskCategory(trait.getRiskCategory())
                        .role(trait.getRole())
                        .build())
                .collect(Collectors.toList());
    }

}
