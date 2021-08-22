package com.example.siemcenter.alarms.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmSearchDTO {
    private String message;
    private String factStatus;
    private boolean regex;
    private String date;
    private LocalDate startDate;
    private LocalDate endDate;
}
