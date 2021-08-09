package com.example.siemcenter.users.models;

import com.example.siemcenter.constants.SequenceConstants;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @SequenceGenerator(name = SequenceConstants.USER_SEQUENCE, sequenceName = SequenceConstants.USER_SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.USER_SEQUENCE)
    private Long id;
    @NotNull
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
    private String username;
    @NotNull
    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters long")
    private String password;
    private LocalDateTime lastTimeUserWasActive;
    @NotNull
    @Column(name = "risk_category")
    private RiskCategory riskCategory;
    @NotNull
    private Role role;
}
