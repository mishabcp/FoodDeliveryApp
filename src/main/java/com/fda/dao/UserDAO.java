package com.fda.dao;

import com.fda.model.User;

import java.util.List;

public interface UserDAO {
    void addUser(User user);
    User getUserById(int userId);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(int userId);
    int getUserIdByUsername (String username);
}
