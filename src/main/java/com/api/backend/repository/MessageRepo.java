package com.api.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.backend.model.MessageModel;

public interface MessageRepo extends JpaRepository<MessageModel, Integer>{
}