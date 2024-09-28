package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.RolModel;
import com.api.backend.repository.RolRepo;

@Service
public class RolService {

    @Autowired
    private RolRepo rolRepo;
    public int obtainRolList;

    public List<RolModel> obtainRolList() {
        return (List<RolModel>) rolRepo.findAll();
    }

    public RolModel createRol(RolModel rol) {
        return rolRepo.save(rol);
    }

    public RolModel updateRol(RolModel rol) {
        return rolRepo.save(rol);
    }

    public void deleteRol(int id) {
        rolRepo.deleteById(id);
    }
    public List<RolModel> obtainPublicRolList() {
        return rolRepo.findByAccessTrue(); 
    }
}
