package com.example.siemcenter.common.controllers;

import com.example.siemcenter.common.models.OperatingSystem;
import com.example.siemcenter.common.services.OperatingSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/systems")
public class OperatingSystemController {
    private OperatingSystemService service;

    @Autowired
    public OperatingSystemController(OperatingSystemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OperatingSystem>> getAllSystems() {
        List<OperatingSystem> systemList = service.getAll();
        return ResponseEntity.ok(systemList);
    }
}
