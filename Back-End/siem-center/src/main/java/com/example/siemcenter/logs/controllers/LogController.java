package com.example.siemcenter.logs.controllers;

import com.example.siemcenter.logs.dtos.LogDTO;
import com.example.siemcenter.logs.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    }
}
