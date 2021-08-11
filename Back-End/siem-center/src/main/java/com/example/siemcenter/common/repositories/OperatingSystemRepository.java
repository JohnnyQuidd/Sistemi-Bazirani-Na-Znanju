package com.example.siemcenter.common.repositories;

import com.example.siemcenter.common.models.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperatingSystemRepository extends JpaRepository<OperatingSystem, Long> {
    boolean existsByName(String name);
    Optional<OperatingSystem> findByName(String name);
}
