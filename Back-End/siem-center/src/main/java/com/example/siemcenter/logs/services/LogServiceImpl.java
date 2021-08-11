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
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.repositories.LogRepository;
import com.example.siemcenter.users.models.User;
import com.example.siemcenter.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    private LogRepository logRepository;
    private DeviceRepository deviceRepository;
    private SoftwareRepository softwareRepository;
    private OperatingSystemRepository osRepository;
    private UserRepository userRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository,
                          DeviceRepository deviceRepository,
                          SoftwareRepository softwareRepository,
                          OperatingSystemRepository operatingSystemRepository,
                          UserRepository userRepository) {
        this.logRepository = logRepository;
        this.deviceRepository = deviceRepository;
        this.softwareRepository = softwareRepository;
        this.osRepository = operatingSystemRepository;
        this.userRepository = userRepository;
    }

    public void createLog(@Valid @RequestBody LogDTO logDTO){
        Log log = createLogFromDTO(logDTO);
        logRepository.save(log);
    }

    private Log createLogFromDTO(LogDTO logDTO) {
        LocalDateTime timestamp = LocalDateTime.now();
        if(logDTO.getTimestamp() != null) {
            timestamp = logDTO.getTimestamp();
        }

        Device device = extractDevice(logDTO.getIpAddress());
        Software software = extractSoftware(logDTO.getSoftware());
        OperatingSystem os = extractOperatingSystem(logDTO.getOperatingSystem());
        User user = extractUser(logDTO.getUsername());

        return Log.builder()
                .device(device)
                .software(software)
                .os(os)
                .user(user)
                .timestamp(timestamp)
                .logType(logDTO.getLogType())
                .factStatus(FactStatus.ACTIVE)
                .message(logDTO.getMessage())
                .build();

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
}
