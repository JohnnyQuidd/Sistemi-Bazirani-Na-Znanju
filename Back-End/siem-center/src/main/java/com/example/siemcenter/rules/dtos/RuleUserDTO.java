package com.example.siemcenter.rules.dtos;

import com.example.siemcenter.users.models.RiskCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleUserDTO {
    private int numberOfDevices;
    private RiskCategory previousRiskCategory;
    private RiskCategory newRiskCategory;
}
