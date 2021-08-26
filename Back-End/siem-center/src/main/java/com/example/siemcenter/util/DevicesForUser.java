package com.example.siemcenter.util;

import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DevicesForUser {
    private LogRepository logRepository;

    @Autowired
    public DevicesForUser(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public int getNumberOfDevicesForUser(String username) {
        List<Log> logList = logRepository.findByUser_Username(username);
        List<String> devices = new LinkedList<>();

        for (Log log : logList) {
            if (!devices.contains(log.getDevice().getIpAddress())) {
                devices.add(log.getDevice().getIpAddress());
            }
        }

        return devices.size();
    }
}
