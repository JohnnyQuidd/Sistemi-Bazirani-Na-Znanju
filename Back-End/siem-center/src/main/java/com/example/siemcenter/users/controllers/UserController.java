package com.example.siemcenter.users.controllers;

import com.example.siemcenter.users.dtos.UserDTO;
import com.example.siemcenter.users.dtos.UserLoginDTO;
import com.example.siemcenter.users.models.User;
import com.example.siemcenter.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long id) {
        User user = userService.findUserById(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(404).build();
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping
    public void updateUser(@Valid @RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
    }

    @PostMapping(path = "/register")
    public void registerANewUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerANewUser(userDTO);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String role = userService.loginUser(loginDTO);

        if (role == null) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(role);
    }
}
