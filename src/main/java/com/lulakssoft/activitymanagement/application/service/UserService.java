package com.lulakssoft.activitymanagement.application.service;

import com.lulakssoft.activitymanagement.domain.model.user.User;
import com.lulakssoft.activitymanagement.domain.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private User currentUser;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void deleteUser(String userId) {
        userRepository.delete(userId);
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }
    public User findUserById(String userId) {
        return userRepository.findById(userId);
    }
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public void updateUser(User user) {
        userRepository.save(user);
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

}
