package com.lulakssoft.activitymanagement.database;

import com.lulakssoft.activitymanagement.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    User save(User user);
    void delete(User user);
    List<User> findAll();
    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
}