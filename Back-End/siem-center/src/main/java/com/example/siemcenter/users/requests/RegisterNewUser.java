package com.example.siemcenter.users.requests;

import com.example.siemcenter.users.dto.UserDTO;

import javax.validation.constraints.NotNull;


public class RegisterNewUser {
    @NotNull(message = "User's information missing")
    public UserDTO userDTO;
}
