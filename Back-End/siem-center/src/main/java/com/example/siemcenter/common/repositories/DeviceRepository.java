package com.example.siemcenter.common.repositories;

import com.example.siemcenter.common.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface DeviceRepository extends JpaRepository<Device, Long> {
}
