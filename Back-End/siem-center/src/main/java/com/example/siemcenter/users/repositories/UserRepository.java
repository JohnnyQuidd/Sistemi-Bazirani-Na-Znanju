package com.example.siemcenter.users.repositories;

import com.example.siemcenter.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findById(long id);

    @Query(value = "SELECT * FROM USER WHERE ROLE=0", nativeQuery = true)
    List<User> findAllUsers();

    @Query(value = "SELECT * FROM USER WHERE ROLE=1", nativeQuery = true)
    List<User> findAllAdmins();

    @Query(value = "SELECT * FROM USER WHERE ROLE=2", nativeQuery = true)
    List<User> findAllOperators();
}
