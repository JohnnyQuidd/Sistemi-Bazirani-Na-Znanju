package com.example.siemcenter.rules.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.rules.dtos.RuleDeviceDTO;
import com.example.siemcenter.users.models.User;
import org.kie.api.runtime.KieSession;

import java.util.List;

public interface RuleService {
    void insertLog(Log log);

    void insertAlarm(Alarm alarm);

    List<Log> getLogs();

    List<Log> getLogsBySession(KieSession session);

    List<Alarm> getAlarms();

    List<Alarm> getAlarmsBySession(KieSession session);

    List<Alarm> getAlarmForRule(String ruleName);

    List<Alarm> getAlarmForRuleAndSession(String ruleName, KieSession kieSession);

    List<User> getUsersForSixOrMoreAlarms();

    List<User> usersWithMultipleFailedLogins(int deviceNum);

    List<User> getUsersForTenAlarmsInTenPastDays();

    List<Log> fetchLogsByRegex(String regex);

    List<Log> fetchLogsByRegexAndSession(String regex, KieSession session);

    List<Alarm> fetchAlarmsByRegex(String regex);

    List<Alarm> fetchAlarmsByRegexAndSession(String regex, KieSession session);

    void createNewRuleFromUserDeviceDTO(RuleDeviceDTO dto);
}
