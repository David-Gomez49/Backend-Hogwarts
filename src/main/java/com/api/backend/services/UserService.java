package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        List<AuxiliarUserModel> a = (List<AuxiliarUserModel> )userRepo.findAllUsersWithSpecificFields();
        System.out.println("-----------list--------");
        System.out.println(a);
        System.out.println("-----------list--------");
        return a ;
    }
    public UserModel obtainUserById(int studentId) {
        return userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + studentId));
    }

    public UserModel obtainUserByEmail(String email) {
        UserModel user = userRepo.findByEmail(email);
        System.out.println("-----------user--------");
        System.out.println(user);
        System.out.println("-----------user--------");
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

    @Transactional
    public void deleteByEmail(String email) {
        userRepo.deleteByEmail(email);
    }

    public UserModel editRolByEmail(String email, String roleName) {
        UserModel user = userRepo.findByEmail(email);
        
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el email: " + email);
        }

        RolModel newRole = rolRepo.findByName(roleName);

        if (newRole == null) {
            throw new IllegalArgumentException("Rol no encontrado con el nombre: " + roleName);
        }
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

    public AuxiliarUserModel getInfoByEmail(String Email){
        return userRepo.getInfoByEmail(Email);
    }
    
    public RolModel GetRolByEmail(String Email) {
        UserModel user = userRepo.findByEmail(Email);
        return user.getRol();
    }

    public boolean  validateAdmin(String Email) {
        UserModel user = userRepo.findByEmail(Email);
        return user.getRol().getName().equals("Admin");
    }    
    public boolean  validateTeacher(String Email) {
        UserModel user = userRepo.findByEmail(Email);
        return user.getRol().getName().equals("Teacher");
    }    
}
