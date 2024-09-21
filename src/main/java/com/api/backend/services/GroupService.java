package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.GroupModel;
import com.api.backend.repository.GroupRepo;

@Service
public class GroupService {

    @Autowired
    private GroupRepo groupRepo;

    public List<GroupModel> obtainGroupList() {
        return (List<GroupModel>) groupRepo.findAll();
    }

    public GroupModel createGroup(GroupModel group) {
        return groupRepo.save(group);
    }

    public GroupModel updateGroup(GroupModel group) {
        return groupRepo.save(group);
    }

    public void deleteGroup(int id) {
        groupRepo.deleteById(id);
    }
}
