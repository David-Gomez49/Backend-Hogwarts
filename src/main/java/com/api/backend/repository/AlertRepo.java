package com.api.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.AlertModel;

public interface AlertRepo extends JpaRepository<AlertModel, Integer>{

    Optional<AlertModel> findByUser_IdAndClasses_Id(int User_Id,int Classes_Id);
}