package com.example.siemcenter.logs.repositories;

import com.example.siemcenter.logs.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface LogRepository extends JpaRepository<Log, Long> {
}
