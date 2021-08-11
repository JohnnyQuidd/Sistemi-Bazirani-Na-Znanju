package com.example.siemcenter.alarms.services;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.repositories.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmServiceImpl implements AlarmService {
    private AlarmRepository alarmRepository;

    @Autowired
    public AlarmServiceImpl(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Override
    public List<Alarm> findAllAlarms() {
        return alarmRepository.findAll();
    }

    @Override
    public void insertNewAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
    }
}
