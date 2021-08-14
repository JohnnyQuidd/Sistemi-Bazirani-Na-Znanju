package com.example.siemcenter.rules.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.logs.models.Log;

import java.util.List;
import java.util.Map;

public interface RuleService {
    void insertRule(Map<String, Object> ruleData);
    void insertLog(Log log);
    List<Log> getLogs();
    List<Alarm> getAlarms();
    List<Alarm> getAlarmForRule(String ruleName);
}