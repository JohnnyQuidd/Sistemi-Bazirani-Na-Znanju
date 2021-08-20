package com.example.siemcenter.logs.services;

import com.example.siemcenter.common.exceptions.ResourceNotFoundException;
import com.example.siemcenter.common.models.Device;
import com.example.siemcenter.common.models.FactStatus;
import com.example.siemcenter.common.models.OperatingSystem;
import com.example.siemcenter.common.models.Software;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.common.repositories.OperatingSystemRepository;
import com.example.siemcenter.common.repositories.SoftwareRepository;
import com.example.siemcenter.logs.dtos.LogDTO;
import com.example.siemcenter.logs.dtos.LogSearchDTO;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.models.LogType;
import com.example.siemcenter.logs.repositories.LogRepository;
import com.example.siemcenter.rules.services.RuleService;
import com.example.siemcenter.users.models.User;
import com.example.siemcenter.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {
    private LogRepository logRepository;
    private DeviceRepository deviceRepository;
    private SoftwareRepository softwareRepository;
    private OperatingSystemRepository osRepository;
    private UserRepository userRepository;
    private RuleService ruleService;

    @Autowired
    public LogServiceImpl(LogRepository logRepository,
                          DeviceRepository deviceRepository,
                          SoftwareRepository softwareRepository,
                          OperatingSystemRepository operatingSystemRepository,
                          UserRepository userRepository,
                          RuleService ruleService) {
        this.logRepository = logRepository;
        this.deviceRepository = deviceRepository;
        this.softwareRepository = softwareRepository;
        this.osRepository = operatingSystemRepository;
        this.userRepository = userRepository;
        this.ruleService = ruleService;
    }

    public void createLog(@Valid @RequestBody LogDTO logDTO) {
        Log log = createLogFromDTO(logDTO);
        logRepository.save(log);
        ruleService.insertLog(log);
    }

    public Log createLogFromDTO(LogDTO logDTO) {
        LocalDateTime timestamp = LocalDateTime.now();
        if (logDTO.getTimestamp() != null) {
            timestamp = logDTO.getTimestamp();
        }

        Device device = extractDevice(logDTO.getIpAddress());
        Software software = extractSoftware(logDTO.getSoftware());
        OperatingSystem os = extractOperatingSystem(logDTO.getOperatingSystem());
        User user = extractUser(logDTO.getUsername());

        return Log.builder()
                .uuid(UUID.randomUUID())
                .device(device)
                .software(software)
                .os(os)
                .user(user)
                .timestamp(timestamp)
                .logType(logDTO.getLogType())
                .factStatus(FactStatus.ACTIVE)
                .message(logDTO.getMessage())
                ._timestamp(new Date())
                .build();

    }

    @Override
    public List<Log> searchLogs(LogSearchDTO logDTO) {
        logDTO = formatDTO(logDTO);
        List<Log> logList = fetchLogs(logDTO);

        if(!logDTO.getLogType().equals("")) {
            LogSearchDTO finalLogDTO = logDTO;
            logList = logList.stream()
                    .filter(log -> log.getLogType() == LogType.valueOf(finalLogDTO.getLogType()))
                    .collect(Collectors.toList());
        }

        if(!logDTO.getFactStatus().equals("")) {
            LogSearchDTO finalLogDTO1 = logDTO;
            logList = logList.stream()
                    .filter(log -> log.getFactStatus() == FactStatus.valueOf(finalLogDTO1.getFactStatus()))
                    .collect(Collectors.toList());
        }

        if(logDTO.getStartDate() != null) {
            LogSearchDTO finalLogDTO2 = logDTO;
            logList = logList.stream()
                    .filter(log -> log.getTimestamp().toLocalDate().isAfter(finalLogDTO2.getStartDate()))
                    .collect(Collectors.toList());
        }

        if(logDTO.getEndDate() != null) {
            LogSearchDTO finalLogDTO3 = logDTO;
            logList = logList.stream()
                    .filter(log -> log.getTimestamp().toLocalDate().isBefore(finalLogDTO3.getEndDate()))
                    .collect(Collectors.toList());
        }

        return logList;
    }

    private LogSearchDTO formatDTO(LogSearchDTO dto) {
        dto.setMessage(dto.getMessage().trim());
        String[] date = dto.getDate().split(",");

        String startString = date[0].substring(1);
        Timestamp startTimestamp = new Timestamp(Long.parseLong(startString));
        LocalDate start = startTimestamp.toLocalDateTime().toLocalDate();

        String endString = date[1].substring(0, date[1].length()-1);
        Timestamp endTimestamp = new Timestamp(Long.parseLong(endString));
        LocalDate end = endTimestamp.toLocalDateTime().toLocalDate();

        dto.setStartDate(start);
        dto.setEndDate(end);

        return dto;
    }

    private List<Log> fetchLogs(LogSearchDTO logDTO) {
        if(!logDTO.isRegex() && !logDTO.getMessage().equals("")) {
            return logRepository.findByMessageContains(logDTO.getMessage());
        }
        if(logDTO.isRegex() && !logDTO.getMessage().equals("")) {
            return ruleService.fetchLogsByRegex(logDTO.getMessage());
        }

        return logRepository.findAll();
    }

    private Device extractDevice(String ipAddress) {
        return deviceRepository.findByIpAddress(ipAddress)
                .orElseGet(() -> {
                    Device dev = new Device(ipAddress);
                    deviceRepository.save(dev);
                    return dev;
                });
    }

    private Software extractSoftware(String name) {
        return softwareRepository.findSoftwareByName(name)
                .orElseGet(() -> {
                    Software software = new Software(name);
                    softwareRepository.save(software);
                    return software;
                });
    }

    private OperatingSystem extractOperatingSystem(String name) {
        return osRepository.findByName(name)
                .orElseGet(() -> {
                    OperatingSystem os = new OperatingSystem(name);
                    osRepository.save(os);
                    return os;
                });
    }

    private User extractUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with provided username cannot be fetched"));
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    @Override
    public void update(Log log) {
        logRepository.save(log);
    }
}
