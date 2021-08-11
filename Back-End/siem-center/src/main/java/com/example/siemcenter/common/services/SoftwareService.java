package com.example.siemcenter.common.services;

import com.example.siemcenter.common.models.Software;

import java.util.List;

public interface SoftwareService {
    List<Software> getAllSoftware();
    Software getSoftwareByName(String name);
    void insertNewSoftware(Software software);
}
