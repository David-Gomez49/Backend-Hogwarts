package com.api.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.backend.model.StudentWithGroupModel;
import com.api.backend.model.UserModel;
import com.api.backend.model.UserxGroupModel;

public interface UserXGroupRepo extends JpaRepository<UserxGroupModel, Integer>{
        UserxGroupModel findByStudent(UserModel student);

        @Query("SELECT new com.api.backend.model.StudentWithGroupModel(u, ug.group) " +
       "FROM UserModel u " +
       "LEFT JOIN UserxGroupModel ug ON u.id = ug.student.id " +
       "LEFT JOIN GroupModel g ON ug.group.id = g.id " +
       "WHERE u.rol.name = 'Student'")
        List<StudentWithGroupModel> findAllStudentsWithGroup();


        @Query("SELECT u FROM UserModel u " +
        "LEFT JOIN UserxGroupModel ug ON u.id = ug.student.id " +
        "LEFT JOIN GroupModel g ON ug.group.id = g.id " +
        "WHERE u.rol.name = 'Student' AND g.id = :groupId")
        List<UserModel> findStudentsByGroupId(int groupId);

        Optional<UserxGroupModel> findByStudentId(int studentId);

}