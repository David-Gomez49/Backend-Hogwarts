package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.RolModel;
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

    public boolean InfoCompleteByEmail(String Email) {
        UserModel user = userRepo.findByEmail(Email);
        if (user.getName() == null || user.getName().isEmpty()
                || user.getLastname() == null || user.getLastname().isEmpty()
                || user.getPhone() == null || user.getPhone().isEmpty()
                || user.getAddress() == null || user.getAddress().isEmpty()
                || user.getBirthday() == null || user.getGender() == null
                || user.getGender().isEmpty() || user.getPicture() == null
                || user.getPicture().isEmpty() || user.getRol() == null
                || user.getDocument_type() == null || user.getDocument_type().isEmpty()
                || user.getDocument_number() == null || user.getDocument_number().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public UserModel updateUserByEmail(String email, UserModel updatedUser) {
        // Buscar el usuario actual por email
        UserModel existingUser = userRepo.findByEmail(email);

        if (existingUser == null) {
            throw new RuntimeException("Usuario no encontrado con el email: " + email);
        }

        // Actualizar los campos restantes del usuario
        existingUser.setRol(updatedUser.getRol());  // Ejemplo
        existingUser.setPhone(updatedUser.getPhone());  // Ejemplo
        existingUser.setBirthday(updatedUser.getBirthday());  // Ejemplo
        existingUser.setAddress(updatedUser.getAddress());  // Ejemplo
        existingUser.setGender(updatedUser.getGender());  // Ejemplo
        existingUser.setDocument_type(updatedUser.getDocument_type());  // Ejemplo
        existingUser.setDocument_number(updatedUser.getDocument_number());  // Ejemplo

        // Guardar los cambios
        return userRepo.save(existingUser);
    }

    public boolean existsByEmail(String Email) {
        return userRepo.existsByEmail(Email);
    }

    public RolModel GetRolByEmail(String Email) {
        UserModel user = userRepo.findByEmail(Email);
        return user.getRol();
    }
}
