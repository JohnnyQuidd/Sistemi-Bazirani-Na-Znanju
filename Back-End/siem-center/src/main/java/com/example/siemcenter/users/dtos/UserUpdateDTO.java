package com.example.siemcenter.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {
    @NotNull
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
    private String username;
    @NotBlank
    private String role = "USER";
    @NotBlank
    private String riskCategory;
}
