package com.example.siemcenter.logs.models;

import com.example.siemcenter.common.models.Device;
import com.example.siemcenter.common.models.FactStatus;
import com.example.siemcenter.common.models.OperatingSystem;
import com.example.siemcenter.common.models.Software;
import com.example.siemcenter.users.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Role(Role.Type.EVENT)
@Timestamp("_timestamp")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private UUID uuid;
    private String message;
    private LocalDateTime timestamp;
    private LogType logType;
    private FactStatus factStatus;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "software_id")
    private Software software;

    @ManyToOne
    @JoinColumn(name = "os_id")
    private OperatingSystem os;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Date _timestamp;
}
