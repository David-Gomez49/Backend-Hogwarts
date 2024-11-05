package com.api.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.api.backend.model.StudentWithGroupModel;
import com.api.backend.model.UserModel;
import com.api.backend.model.UserxGroupModel;

public interface UserXGroupRepo extends JpaRepository<UserxGroupModel, Integer>{
        UserxGroupModel findByStudent(UserModel student);

        @Query("SELECT new com.api.backend.model.StudentWithGroupModel(u, COALESCE(ug.group, null)) " +
       "FROM UserModel u " +
       "LEFT JOIN UserxGroupModel ug ON u.id = ug.student.id " +
       "WHERE u.rol.name = 'Student'")
        List<StudentWithGroupModel> findAllStudentsWithGroup();
 

        Optional<UserxGroupModel> findByStudentId(int studentId);

}