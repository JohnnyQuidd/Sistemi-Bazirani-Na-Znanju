package com.example.siemcenter.logs.controllers;

import com.example.siemcenter.logs.dtos.LogDTO;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {
    private LogService logsService;

    @Autowired
    public LogController(LogService logsService) {
        this.logsService = logsService;
    }

    @PostMapping
    public void insertNewLog(@Valid @RequestBody LogDTO logDTO, HttpServletRequest request) {
        logDTO.setIpAddress(request.getRemoteAddr());
        logsService.createLog(logDTO);
    }

    @GetMapping
    public ResponseEntity<List<Log>> getAllLogs() {
        List<Log> logs = logsService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}
