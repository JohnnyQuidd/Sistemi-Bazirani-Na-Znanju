package com.example.siemcenter.users.drools;

import com.example.siemcenter.users.models.RiskCategory;
import com.example.siemcenter.users.models.Role;
import com.example.siemcenter.users.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.drools.core.factmodel.traits.Trait;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Trait
public class UserTrait {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
    private String username;
    @NotNull
    @Size(min = 5, message = "Password must be minimum 20 characters long")
    @JsonIgnore
    private String password;
    private LocalDateTime lastTimeUserWasActive;
    @NotNull
    @Column(name = "risk_category")
    private RiskCategory riskCategory;
    @NotNull
    private Role role;
    private String ruleTriggered;
    private int numberOfAlarms;

    public UserTrait(User user, int numberOfAlarms) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.lastTimeUserWasActive = user.getLastTimeUserWasActive();
        this.riskCategory = user.getRiskCategory();
        this.role = user.getRole();
        this.numberOfAlarms = numberOfAlarms;
    }
}
