package com.example.siemcenter.logs.repositories;

import com.example.siemcenter.logs.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface LogRepository extends JpaRepository<Log, Long> {
    @Query(value = "SELECT * FROM LOG WHERE MESSAGE LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Log> findByMessageContains(String partOfAMessage);

    List<Log> findByDevice_IpAddress(String ipAddress);

    List<Log> findByOs_Name(String chosenSystem);
}
