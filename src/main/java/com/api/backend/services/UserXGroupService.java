package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.UserxGroupModel;
import com.api.backend.repository.UserXGroupRepo;

@Service
public class UserXGroupService {

    @Autowired
    private UserXGroupRepo userxgroupRepo;

    public List<UserxGroupModel> obtainUserXGroupList() {
        return (List<UserxGroupModel>) userxgroupRepo.findAll();
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
}
