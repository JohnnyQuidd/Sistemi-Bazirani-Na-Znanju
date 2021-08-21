package com.example.siemcenter.alarms.repositories;

import com.example.siemcenter.alarms.models.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query(value = "SELECT * FROM ALARM WHERE MESSAGE LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Alarm> findByMessageContains(String message);
}
