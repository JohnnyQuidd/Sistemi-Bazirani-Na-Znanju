package com.example.siemcenter.rules.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.rules.dtos.RuleUserDTO;
import com.example.siemcenter.users.models.User;

import java.util.List;

public interface RuleService {
    void insertLog(Log log);

    void insertAlarm(Alarm alarm);

    List<Log> getLogs();

    List<Alarm> getAlarms();

    List<Alarm> getAlarmForRule(String ruleName);

    List<User> getUsersForSixOrMoreAlarms();

    List<User> usersWithMultipleFailedLogins(int deviceNum);

    List<User> getUsersForTenAlarmsInTenPastDays();

    List<Log> fetchLogsByRegex(String regex);

    List<Alarm> fetchAlarmsByRegex(String regex);

    void createNewRuleFromUserDeviceDTO(RuleUserDTO dto);
}
