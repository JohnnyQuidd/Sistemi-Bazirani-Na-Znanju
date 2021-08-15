package com.example.siemcenter.logs.services;

import com.example.siemcenter.logs.dtos.LogDTO;
import com.example.siemcenter.logs.models.Log;

import java.util.List;

public interface LogService {
    void createLog(LogDTO logDTO);
    List<Log> getAllLogs();
    void update(Log log);
    Log createLogFromDTO(LogDTO logDTO);
}
