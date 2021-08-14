package com.example.siemcenter.logs.dtos;

import com.example.siemcenter.logs.models.LogType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogDTO {
    @NotNull
    private LogType logType;
    private String ipAddress;
    @NotBlank
    private String operatingSystem;
    @NotBlank
    private String software;
    @NotBlank
    private String username;
    private LocalDateTime timestamp;
    private String message;
}
