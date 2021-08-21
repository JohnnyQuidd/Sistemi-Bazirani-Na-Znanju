package com.example.siemcenter.common.services.implementations;

import com.example.siemcenter.common.models.OperatingSystem;
import com.example.siemcenter.common.repositories.OperatingSystemRepository;
import com.example.siemcenter.common.services.OperatingSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatingSystemServiceImpl implements OperatingSystemService {
    private OperatingSystemRepository repository;

    @Autowired
    public OperatingSystemServiceImpl(OperatingSystemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<OperatingSystem> getAll() {
        return repository.findAll();
    }
}
