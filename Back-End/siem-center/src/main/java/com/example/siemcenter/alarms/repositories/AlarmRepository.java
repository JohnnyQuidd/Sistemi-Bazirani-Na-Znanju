package com.example.siemcenter.alarms.repositories;

import com.example.siemcenter.alarms.models.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
