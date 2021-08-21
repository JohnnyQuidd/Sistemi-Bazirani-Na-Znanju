package com.example.siemcenter.alarms.controllers;

import com.example.siemcenter.alarms.dtos.AlarmSearchDTO;
import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alarms")
public class AlarmController {
    private AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @GetMapping
    public ResponseEntity<List<Alarm>> getAllAlarms() {
        return ResponseEntity.ok(alarmService.findAllAlarms());
    }

    @PostMapping("/search")
    public ResponseEntity<List<Alarm>> getAlarmsForProvidedCriteria(@RequestBody AlarmSearchDTO alarmDTO) {
        List<Alarm> alarmList = alarmService.findAlarmsThatMatchCriteria(alarmDTO);
        return ResponseEntity.ok(alarmList);
    }
}
