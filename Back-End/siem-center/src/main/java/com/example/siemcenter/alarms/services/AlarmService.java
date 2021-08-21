package com.example.siemcenter.alarms.services;

import com.example.siemcenter.alarms.dtos.AlarmSearchDTO;
import com.example.siemcenter.alarms.models.Alarm;

import java.util.List;

public interface AlarmService {
    List<Alarm> findAllAlarms();

    void insertNewAlarm(Alarm alarm);

    List<Alarm> findAlarmsThatMatchCriteria(AlarmSearchDTO alarmDTO);
}
