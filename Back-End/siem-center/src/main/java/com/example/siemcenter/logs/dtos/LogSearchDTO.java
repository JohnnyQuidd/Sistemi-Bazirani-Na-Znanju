package com.example.siemcenter.logs.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogSearchDTO {
    private String message;
    private String factStatus;
    private String logType;
    private boolean regex;
    private LocalDate[] date;
}
