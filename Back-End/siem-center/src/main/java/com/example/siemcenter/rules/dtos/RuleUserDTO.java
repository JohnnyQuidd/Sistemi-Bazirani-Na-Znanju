package com.example.siemcenter.rules.dtos;

import com.example.siemcenter.users.models.RiskCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleUserDTO {
    @NotNull
    @Min(value = 1, message = "Number of devices cannot be less than 1")
    private int numberOfDevices;
    @NotNull
    private RiskCategory previousRiskCategory;
    @NotNull
    private RiskCategory newRiskCategory;
    @NotNull
    @Min(value = 1, message = "Number of days cannot be less than 1")
    private int days;
}
