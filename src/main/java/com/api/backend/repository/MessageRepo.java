package com.api.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.backend.model.MessageModel;

public interface MessageRepo extends JpaRepository<MessageModel, Integer> {
    List<MessageModel> findByClasses_Id(int chatGroupId);  // Para obtener los mensajes de un grupo
}
