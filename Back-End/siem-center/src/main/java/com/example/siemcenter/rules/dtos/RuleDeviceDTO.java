package com.example.siemcenter.rules.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleDeviceDTO {
    @NotNull
    @Min(value = 0, message = "Number of logs cannot be less than 0")
    private int numberOfLogs;
    @NotBlank
    private String ipAddress;
    @NotNull
    @Min(value = 1, message = "Number of days cannot be less than 1")
    private int days;
}
