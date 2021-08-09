package com.example.siemcenter.users.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @NotNull
    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
    private String username;
    @NotNull
    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters long")
    private String password;
    @NotNull
    private String role;
}
