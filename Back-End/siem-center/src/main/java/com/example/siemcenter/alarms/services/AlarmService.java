package com.example.siemcenter.alarms.services;

import com.example.siemcenter.alarms.models.Alarm;

import java.util.List;

public interface AlarmService {
    List<Alarm> findAllAlarms();

    void insertNewAlarm(Alarm alarm);
}
