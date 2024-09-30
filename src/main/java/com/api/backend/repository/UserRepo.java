package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.backend.model.UserModel;

public interface UserRepo extends JpaRepository<UserModel, Integer>{
    boolean existsByEmail(String email);
    UserModel findByEmail(String email);

    @Query("SELECT u.name, u.lastname, u.email, u.document_number, u.rol FROM UserModel u")
    List<UserModel> findAllUsersWithSpecificFields();

    @Query("SELECT u.name, u.lastname, u.picture, u.rol FROM UserModel u WHERE u.email = :email")
    UserModel getInfoByEmail(@Param("email") String email);

}
