package com.example.siemcenter.rules.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.services.AlarmService;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.rules.repositories.RuleRepository;
import com.example.siemcenter.users.drools.UserTrait;
import com.example.siemcenter.users.models.User;
import com.example.siemcenter.users.repositories.UserRepository;
import org.drools.core.ClockType;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RuleServiceImpl implements RuleService {
    private Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);
    private RuleRepository ruleRepository;
    private KieSession session;
    private DeviceRepository deviceRepository;
    private AlarmService alarmService;
    private UserRepository userRepository;
    private static Integer DEVICE_NUMBER = 1;


    @Autowired
    public RuleServiceImpl(RuleRepository ruleRepository,
                           DeviceRepository deviceRepository,
                           AlarmService alarmService,
                           UserRepository userRepository) {
        this.ruleRepository = ruleRepository;
        this.deviceRepository = deviceRepository;
        this.alarmService = alarmService;
        this.userRepository = userRepository;

        session = setUpSessionForStreamProcessingMode();

        session.setGlobal("logger", logger);
        session.setGlobal("deviceRepository", deviceRepository);
        session.setGlobal("alarmService", alarmService);
        session.setGlobal("userRepository", userRepository);
        session.setGlobal("deviceNumber", DEVICE_NUMBER);
    }

    private KieSession setUpSessionForStreamProcessingMode() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.newKieClasspathContainer();

        KieSessionConfiguration ksconf = ks.newKieSessionConfiguration();
        ksconf.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));

        KieSession session = kc.newKieSession("session1", ksconf);
        return session;
    }

    @Override
    public void insertRule(Map<String, Object> ruleData) {

    }

    @Override
    public void insertLog(Log log) {
        session.insert(log);
        session.fireAllRules();
    }

    @Override
    public List<Log> getLogs() {
        List<Log> logs = new LinkedList<>();
        QueryResults allLogs = this.session.getQueryResults("fetchAllLogs");
        for (QueryResultsRow singleRow : allLogs) {
            logs.add((Log) singleRow.get("$allLogs"));
        }
        return logs;
    }

    @Override
    public List<Alarm> getAlarms() {
        List<Alarm> alarms = new LinkedList<>();
        QueryResults allAlarms = session.getQueryResults("fetchAllAlarms");
        for (QueryResultsRow singleRow : allAlarms) {
            alarms.add((Alarm) singleRow.get("$allAlarms"));
        }
        return alarms;
    }

    @Override
    public List<Alarm> getAlarmForRule(String ruleName) {
        List<Alarm> alarms = new LinkedList<>();
        QueryResults allAlarms = session.getQueryResults("fetchAlarmsForRuleName", ruleName);
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

    private List<User> fetchUsersForRule(int ruleNumber) {
        List<UserTrait> users = new LinkedList<>();
        QueryResults userResults = session.getQueryResults("fetchUsersForReportCreation");
        for(QueryResultsRow singleRow : userResults) {
            UserTrait trait = (UserTrait) singleRow.get("$user");
            if(trait.getRuleTriggered().equals("#" + ruleNumber)) {
                users.add(trait);
            }
        }

        return fromTraitToModel(users);
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
