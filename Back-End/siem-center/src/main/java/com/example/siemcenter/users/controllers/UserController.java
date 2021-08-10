package com.example.siemcenter.users.controllers;

import com.example.siemcenter.users.dto.UserDTO;
import com.example.siemcenter.users.dto.UserLoginDTO;
import com.example.siemcenter.users.requests.RegisterNewUser;
import com.example.siemcenter.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String role = userService.loginUser(loginDTO);

        if(role == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(role);
    }
}
