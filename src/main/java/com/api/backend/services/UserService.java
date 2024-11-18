package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.AuxiliarUserModel;
import com.api.backend.model.RolModel;
import com.api.backend.model.UserModel;
import com.api.backend.repository.RolRepo;
import com.api.backend.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RolRepo rolRepo;

    public List<UserModel> obtainUserList() {
        return (List<UserModel>) userRepo.findAll();
    }
    public List<UserModel> obtainTeacherList() {
        //solo roles de teahcer o admin
        return (List<UserModel>) userRepo.findTeachersAndAdmins();
    }
    public List<UserModel> obtainStudentsList() {
        return (List<UserModel>) userRepo.findStudents();
    }
    public List<UserModel> obtainParentsList() {
        return (List<UserModel>) userRepo.findParents();
    }
    
    public List<AuxiliarUserModel> obtainUserListWithSpecificFields() {
        return (List<AuxiliarUserModel> )userRepo.findAllUsersWithSpecificFields();
      
    }
    public UserModel obtainUserById(int studentId) {
        return userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + studentId));
    }

    public UserModel obtainUserByEmail(String email) {
        return userRepo.findByEmail(email);
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

    @Transactional
    public void deleteByEmail(String email) {
        userRepo.deleteByEmail(email);
    }

    public UserModel editRolByEmail(String email, String roleName) {
        UserModel user = userRepo.findByEmail(email);
        RolModel newRole = rolRepo.findByName(roleName);
        user.setRol(newRole);      
        return userRepo.save(user); 
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
        UserModel existingUser = userRepo.findByEmail(email);
        if (existingUser == null) {
            throw new RuntimeException("Usuario no encontrado con el email: " + email);
        }
        if (updatedUser.getRol() != null) {
            existingUser.setRol(updatedUser.getRol());
        }
        if (updatedUser.getPhone() != null) {
            existingUser.setPhone(updatedUser.getPhone());
        }
        if (updatedUser.getBirthday() != null) {
            existingUser.setBirthday(updatedUser.getBirthday());
        }
        if (updatedUser.getAddress() != null) {
            existingUser.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getGender() != null) {
            existingUser.setGender(updatedUser.getGender());
        }
        if (updatedUser.getDocument_type() != null) {
            existingUser.setDocument_type(updatedUser.getDocument_type());
        }
        if (updatedUser.getDocument_number() != null) {
            existingUser.setDocument_number(updatedUser.getDocument_number());
        }
    
        return userRepo.save(existingUser);
    }
    

    public boolean existsByEmail(String Email) {
        return userRepo.existsByEmail(Email);
    }

    public AuxiliarUserModel getInfoByEmail(String Email){
        return userRepo.getInfoByEmail(Email);
    }
    
    public RolModel GetRolByEmail(String Email) {
        return userRepo.findByEmail(Email).getRol();
    }

    public boolean  validateAdmin(String Email) {
        return (userRepo.findByEmail(Email).getRol().getName()).equals("Admin");
         
    }    
    public boolean  validateTeacher(String Email) {
        return  userRepo.findByEmail(Email).getRol().getName().equals("Teacher");
    }    
}
