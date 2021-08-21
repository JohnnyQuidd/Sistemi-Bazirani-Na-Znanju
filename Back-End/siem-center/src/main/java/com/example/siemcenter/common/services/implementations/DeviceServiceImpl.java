package com.example.siemcenter.common.services.implementations;

import com.example.siemcenter.common.exceptions.ResourceNotFoundException;
import com.example.siemcenter.common.models.Device;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.common.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public Device getDeviceByIpAddress(String ipAddress) {
        return deviceRepository.findByIpAddress(ipAddress)
                .orElseThrow(() -> new ResourceNotFoundException("Device with provided Ip Address doesn't exist"));
    }

    @Override
    public void insertNewDevice(Device device) {
        deviceRepository.save(device);
    }
}
