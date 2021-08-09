package com.example.siemcenter.users.services;

import com.example.siemcenter.users.dto.UserDTO;
import com.example.siemcenter.users.models.User;

import java.util.UUID;

public interface UserService {
    void saveUser(User user);
    User getUserByUsername(String username);
    User getUserById(long id);
    void registerANewUser(UserDTO userDTO);
}
