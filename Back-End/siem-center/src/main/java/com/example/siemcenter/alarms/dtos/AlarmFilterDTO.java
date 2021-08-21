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
public class AlarmFilterDTO {
    private boolean logsPerMachine;
    private boolean logsPerSystem;
    private String chosenDevice;
    private String chosenSystem;
    private String date;
    private LocalDate startDate;
    private LocalDate endDate;
}
