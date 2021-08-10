package com.example.siemcenter.logs.services;

import com.example.siemcenter.logs.dtos.LogDTO;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.repositories.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Service
public class LogsServices {
    private LogsRepository logsRepository;

    @Autowired
    public LogsServices(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    @PostMapping
    public void createLog(@Valid @RequestBody LogDTO logDTO){
        Log log = createLogFromDTO(logDTO);
        // logsRepository.save(log);
    }

    private Log createLogFromDTO(LogDTO logDTO) {
        LocalDateTime timestamp = LocalDateTime.now();
        if(logDTO.getTimestamp() != null) {
            timestamp = logDTO.getTimestamp();
        }

        // TODO: Finish creating new log
        return null;
    }
}
