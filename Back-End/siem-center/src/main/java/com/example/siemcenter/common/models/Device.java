package com.example.siemcenter.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device implements Comparable<Device> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String ipAddress;
    private boolean isMalicious;

    @Override
    public int compareTo(Device device) {
        return device.getIpAddress().compareTo(this.getIpAddress());
    }
}
