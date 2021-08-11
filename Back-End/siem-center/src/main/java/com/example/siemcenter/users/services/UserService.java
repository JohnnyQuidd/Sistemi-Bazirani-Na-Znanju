package com.example.siemcenter.users.services;

import com.example.siemcenter.users.dtos.UserDTO;
import com.example.siemcenter.users.dtos.UserLoginDTO;
import com.example.siemcenter.users.models.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    User getUserByUsername(String username);
    User getUserById(long id);
    void registerANewUser(UserDTO userDTO);
    String loginUser(UserLoginDTO loginDTO);
    User findUserById(long id);
    List<User> getAllUsers();
    void updateUser(UserDTO userDTO);
}
