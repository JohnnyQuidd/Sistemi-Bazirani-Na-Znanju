package com.example.siemcenter.rules.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.users.models.User;

import java.util.List;
import java.util.Map;

public interface RuleService {
    void insertRule(Map<String, Object> ruleData);

    void insertLog(Log log);

    List<Log> getLogs();

    List<Alarm> getAlarms();

    List<Alarm> getAlarmForRule(String ruleName);

    List<User> getUsersForSixOrMoreAlarms();

    List<User> usersWithMultipleFailedLogins(int deviceNum);

    List<User> getUsersForTenAlarmsInTenPastDays();

    List<Log> fetchLogsByRegex(String regex);

    List<Alarm> fetchAlarmsByRegex(String regex);
}
