package com.example.siemcenter.common.controllers;

import com.example.siemcenter.common.models.Device;
import com.example.siemcenter.common.services.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/device")
public class DeviceController {
    private DeviceServiceImpl deviceServiceImpl;

    @Autowired
    public DeviceController(DeviceServiceImpl deviceServiceImpl) {
        this.deviceServiceImpl = deviceServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> deviceList = deviceServiceImpl.getAllDevices();
        return ResponseEntity.ok(deviceList);
    }

    @GetMapping("/{ipAddress}")
    public ResponseEntity<Device> getDeviceByIpAddress(@PathVariable("ipAddress") String ipAddress) {
        Device device = deviceServiceImpl.getDeviceByIpAddress(ipAddress);

        return ResponseEntity.ok(device);
    }

    @PostMapping
    public void insertNewIpAddress(@RequestBody Device device) {
        deviceServiceImpl.insertNewDevice(device);
    }
}
