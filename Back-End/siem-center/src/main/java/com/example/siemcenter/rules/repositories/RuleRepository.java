package com.example.siemcenter.rules.repositories;

import com.example.siemcenter.rules.models.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface RuleRepository extends JpaRepository<Rule, Long> {
}
