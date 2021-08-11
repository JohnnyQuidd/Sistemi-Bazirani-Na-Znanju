package com.example.siemcenter.common.services;

import com.example.siemcenter.common.exceptions.ResourceNotFoundException;
import com.example.siemcenter.common.models.Software;
import com.example.siemcenter.common.repositories.SoftwareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoftwareServiceImpl implements SoftwareService {
    private SoftwareRepository softwareRepository;

    @Autowired
    public SoftwareServiceImpl(SoftwareRepository softwareRepository) {
        this.softwareRepository = softwareRepository;
    }

    @Override
    public List<Software> getAllSoftware() {
        return softwareRepository.findAll();
    }

    @Override
    public Software getSoftwareByName(String name) {
        return softwareRepository.findSoftwareByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Software with provided name doesn't exist"));
    }

    @Override
    public void insertNewSoftware(Software software) {
        softwareRepository.save(software);
    }
}
