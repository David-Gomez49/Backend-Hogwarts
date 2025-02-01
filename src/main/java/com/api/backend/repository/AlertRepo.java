package com.api.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.AlertModel;

public interface AlertRepo extends JpaRepository<AlertModel, Integer> {

    Optional<AlertModel> findOptionalByUser_IdAndClasses_Id(int userId, int classesId);

    AlertModel findByUser_IdAndClasses_Id(int userId, int classesId);
}
