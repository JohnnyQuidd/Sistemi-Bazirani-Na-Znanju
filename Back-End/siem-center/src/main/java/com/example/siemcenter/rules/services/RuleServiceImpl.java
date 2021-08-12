package com.example.siemcenter.rules.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.common.models.FactStatus;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.services.LogService;
import com.example.siemcenter.rules.repositories.RuleRepository;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class RuleServiceImpl implements RuleService {
    private RuleRepository ruleRepository;
    private LogService logService;
    //private final DataProviderCompiler compiler;
    private final KieHelper kieHelper;
    private final KieBaseConfiguration baseConfiguration;
    private final KieContainer kieContainer;
    private KieSession session;

    @Autowired
    public RuleServiceImpl(RuleRepository ruleRepository,
                           LogService logService,
                           KieHelper kieHelper,
                           KieBaseConfiguration baseConfiguration,
                           KieContainer kieContainer,
                           KieSession session) {
        this.ruleRepository = ruleRepository;
        this.logService = logService;
        //this.compiler = compiler;
        this.kieHelper = kieHelper;
        this.baseConfiguration = baseConfiguration;
        this.kieContainer = kieContainer;
        this.session = session;
    }

    @Override
    public void insertRule(Map<String, Object> ruleData) {

    }

    @Override
    public void insertLog(Log log) {
        this.session.insert(log);
        log.setFactStatus(FactStatus.ACTIVE);
        this.logService.update(log);
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
