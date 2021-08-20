package com.example.siemcenter.common.services;

import com.example.siemcenter.common.models.Device;

import java.util.List;

public interface DeviceService {
    List<Device> getAllDevices();

    Device getDeviceByIpAddress(String ipAddress);

    void insertNewDevice(Device device);
}
