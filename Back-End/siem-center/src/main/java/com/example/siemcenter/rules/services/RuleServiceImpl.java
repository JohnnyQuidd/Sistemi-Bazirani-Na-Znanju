package com.example.siemcenter.rules.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.services.AlarmService;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.rules.repositories.RuleRepository;
import com.example.siemcenter.users.services.UserService;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RuleServiceImpl implements RuleService {
    private Logger logger = LoggerFactory.getLogger(RuleServiceImpl.class);
    private RuleRepository ruleRepository;
    private KieSession session;
    private DeviceRepository deviceRepository;
    private AlarmService alarmService;


    @Autowired
    public RuleServiceImpl(RuleRepository ruleRepository,
                           DeviceRepository deviceRepository,
                           AlarmService alarmService) {
        this.ruleRepository = ruleRepository;
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.newKieClasspathContainer();
        session = kc.newKieSession();

        session.setGlobal("logger", logger);
        session.setGlobal("deviceRepository", deviceRepository);
        session.setGlobal("alarmService", alarmService);
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
        QueryResults allLogs = this.session.getQueryResults( "fetchAllLogs" );
        for ( QueryResultsRow singleRow : allLogs ) {
            logs.add((Log) singleRow.get("$allLogs"));
        }
        return logs;
    }

    @Override
    public List<Alarm> getAlarms() {
        List<Alarm> alarms = new LinkedList<>();
        QueryResults allAlarms = this.session.getQueryResults( "fetchAllAlarms" );
        for ( QueryResultsRow singleRow : allAlarms ) {
            alarms.add((Alarm) singleRow.get("$allAlarms"));
        }
        return alarms;
    }

    @Override
    public List<Alarm> getAlarmForRule(String ruleName) {
        List<Alarm> alarms = new LinkedList<>();
        QueryResults allAlarms = this.session.getQueryResults( "fetchAlarmsForRuleName", ruleName);
        for ( QueryResultsRow singleRow : allAlarms ) {
            alarms.add((Alarm) singleRow.get("$alarms"));
        }
        return alarms;
    }
}
