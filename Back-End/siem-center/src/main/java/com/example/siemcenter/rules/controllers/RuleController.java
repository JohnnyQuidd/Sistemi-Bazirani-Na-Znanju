package com.example.siemcenter.rules.controllers;

import com.example.siemcenter.rules.dtos.RuleDeviceDTO;
import com.example.siemcenter.rules.services.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rules")
public class RuleController {
    private RuleService ruleService;

    @Autowired
    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("/devices")
    public ResponseEntity<?> creteNewRuleForBlackListingDevice(@Valid @RequestBody RuleDeviceDTO dto) {
        ruleService.createNewRuleFromUserDeviceDTO(dto);
        return ResponseEntity.ok("");
    }
}
