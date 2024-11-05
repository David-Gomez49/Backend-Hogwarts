package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.GroupModel;
import com.api.backend.model.StudentWithGroupModel;
import com.api.backend.model.UserModel;
import com.api.backend.model.UserxGroupModel;
import com.api.backend.repository.UserXGroupRepo;

@Service
public class UserXGroupService {

    @Autowired
    private UserXGroupRepo userxgroupRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    public List<UserxGroupModel> obtainUserXGroupList() {
        return (List<UserxGroupModel>) userxgroupRepo.findAll();
    }

    public UserxGroupModel assignOrUpdateGroup(int studentId, int groupId) {
      
        UserxGroupModel userxGroup = userxgroupRepo.findByStudentId(studentId)
                .orElse(new UserxGroupModel());

        GroupModel group = groupService.obtainById(groupId);

        userxGroup.setStudent(userService.obtainUserById(studentId));
        userxGroup.setGroup(group);

        return userxgroupRepo.save(userxGroup);
    }

    public List<StudentWithGroupModel> getAllStudentsWithGroup() {
        
        return (List<StudentWithGroupModel>) userxgroupRepo.findAllStudentsWithGroup();
    }

    public UserxGroupModel createUserXGroup(UserxGroupModel userxgroup) {
        return userxgroupRepo.save(userxgroup);
    }

    public UserxGroupModel updateUserXGroup(UserxGroupModel userxgroup) {
        return userxgroupRepo.save(userxgroup);
    }

    public void deleteUserXGroup(int id) {
        userxgroupRepo.deleteById(id);
    }

    public UserxGroupModel findByStudent(UserModel student) {
        return userxgroupRepo.findByStudent(student);
    }
}
