package com.example.siemcenter.alarms.controllers;

import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
