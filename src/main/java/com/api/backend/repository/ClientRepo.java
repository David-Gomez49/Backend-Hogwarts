package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.backend.model.ClientModel;

@Repository
public interface ClientRepo extends JpaRepository<ClientModel, Integer> {
}
