package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.backend.model.AuxiliarUserModel;
import com.api.backend.model.UserModel;

public interface UserRepo extends JpaRepository<UserModel, Integer>{
    boolean existsByEmail(String email);

    UserModel findByEmail(String email);
    
    void deleteByEmail(String email);

    @Query(value="SELECT new com.api.backend.model.AuxiliarUserModel(u.name, u.lastname, u.email, u.document_number, u.picture, u.document_type, u.rol) FROM UserModel u")
    List<AuxiliarUserModel> findAllUsersWithSpecificFields();

    @Query(value="SELECT new com.api.backend.model.AuxiliarUserModel(u.name, u.lastname, u.email, u.document_number, u.picture, u.document_type, u.rol) FROM UserModel u WHERE u.email = :email")
    AuxiliarUserModel getInfoByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserModel u WHERE u.role.name = 'Teacher' OR u.role.name = 'Admin'")
    List<UserModel> findTeachersAndAdmins();
}
