package com.lulakssoft.activitymanagement.domain.repository;

import com.lulakssoft.activitymanagement.domain.model.user.User;
import java.util.List;

public interface UserRepository {
    void save(User user);
    void update(User user);
    void delete(String userId);
    User findById(String id);
    User findByUsername(String username);
    List<User> findAll();
}