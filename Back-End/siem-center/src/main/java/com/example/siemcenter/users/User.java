package com.example.siemcenter.users;

import lombok.Data;
import org.springframework.stereotype.Indexed;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(indexes = { @Index(name = "user_index", columnList = "id, username, risk_category")})
public class User {
    @Id
    private UUID id;
    private String username;
    private String password;
    private LocalDateTime lastTimeUserWasActive;
    @Column(name = "risk_category")
    private RiskCategory riskCategory;
    private Role role;
}
