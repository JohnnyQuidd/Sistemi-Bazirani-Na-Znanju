package com.example.siemcenter.users.controllers;

import com.example.siemcenter.users.dto.UserDTO;
import com.example.siemcenter.users.requests.RegisterNewUser;
import com.example.siemcenter.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public void registerANewUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerANewUser(userDTO);
    }
}
