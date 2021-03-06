package com.example.siemcenter.common.repositories;

import com.example.siemcenter.common.models.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SoftwareRepository extends JpaRepository<Software, Long> {
    List<Software> findAll();

    Optional<Software> findSoftwareById(long id);

    Optional<Software> findSoftwareByName(String name);

    boolean existsByName(String name);
}
