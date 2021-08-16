package com.example.siemcenter.common.repositories;

import com.example.siemcenter.common.models.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface OperatingSystemRepository extends JpaRepository<OperatingSystem, Long> {
    boolean existsByName(String name);

    Optional<OperatingSystem> findByName(String name);
}
