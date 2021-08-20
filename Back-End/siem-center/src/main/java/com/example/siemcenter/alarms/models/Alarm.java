package com.example.siemcenter.alarms.models;

import com.example.siemcenter.common.models.FactStatus;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.users.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Role(Role.Type.EVENT)
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String message;
    private LocalDateTime timestamp;
    private FactStatus factStatus;
    private String ruleTriggered;
    private String ipAddress;

    @ManyToMany
    @JoinTable(name = "alarm_log",
            joinColumns = @JoinColumn(name = "alarm_id"),
            inverseJoinColumns = @JoinColumn(name = "log_id")
    )
    private List<Log> relatedLogs = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "alarm_user",
            joinColumns = @JoinColumn(name = "alarm_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> relatedUsers = new LinkedList<>();
}
