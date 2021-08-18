package com.example.siemcenter.users.controllers;

import com.example.siemcenter.rules.services.RuleService;
import com.example.siemcenter.users.dtos.UserDTO;
import com.example.siemcenter.users.dtos.UserLoginDTO;
import com.example.siemcenter.users.dtos.UserUpdateDTO;
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
    private RuleService ruleService;

    @Autowired
    public UserController(UserService userService,
                          RuleService ruleService) {
        this.userService = userService;
        this.ruleService = ruleService;
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
    public void updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userUpdateDTO);
    }

    @PostMapping(path = "/register")
    public void registerANewUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerANewUser(userDTO);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String role = userService.loginUser(loginDTO);
        return role == null ?  ResponseEntity.status(403).build() : ResponseEntity.ok(role);
    }

    @GetMapping(path = "/alarms")
    public ResponseEntity<?> getUsersWithSixOrMoreTriggeredAlarms() {
        List<User> usersForSixOrMoreAlarms = ruleService.getUsersForSixOrMoreAlarms();
        return ResponseEntity.ok(usersForSixOrMoreAlarms);
    }

    @GetMapping(path = "/failedLogin")
    public ResponseEntity<?> getUsersThatFailedToLogInFromNumberOfDevices(@RequestParam("deviceNum") int deviceNum) {
        return ResponseEntity.ok(deviceNum);
    }
}
