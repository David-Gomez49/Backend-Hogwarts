package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.UserModel;
import com.api.backend.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<UserModel> obtainUserList() {
        return (List<UserModel>) userRepo.findAll();
    }

    public UserModel obtainUserByEmail(String email) {
        UserModel user = userRepo.findByEmail(email);
        return user; 
    }

    public UserModel createUser(UserModel user) {
        return userRepo.save(user);
    }

    public UserModel updateUser(UserModel user) {
        return userRepo.save(user);
    }

    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }
    public boolean existsByEmail(String Email)
    {
        return userRepo.existsByEmail(Email);
    }
}
