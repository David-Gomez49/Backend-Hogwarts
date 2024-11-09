package com.api.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.backend.model.CalificationModel;
import com.api.backend.repository.CalificationRepo;

@Service
public class CalificationService {

    @Autowired
    private CalificationRepo calificationRepo;

    public List<CalificationModel> obtainCalificationList() {
        return (List<CalificationModel>) calificationRepo.findAll();
    }

    public List<CalificationModel> getCalificationsListByClass(int Id) {
        return (List<CalificationModel>) calificationRepo.findByAssesment_Classes_Id(Id);
    }

    public List<CalificationModel> getCalificationsByEmail(String Email) {
        return (List<CalificationModel>) calificationRepo.findByStudent_Email(Email);
    }

    public void saveOrUpdateCalifications(List<CalificationModel> califications) {
        for (CalificationModel calification : califications) {
            if ( (calificationRepo.existsById(calification.getId()))) {
                CalificationModel existingCalification = calificationRepo.findById(calification.getId()).orElse(null);
                if (existingCalification != null) {
                    existingCalification.setCalification(calification.getCalification());
                    existingCalification.setAssesment(calification.getAssesment());
                    existingCalification.setStudent(calification.getStudent());
                    calificationRepo.save(existingCalification);
                }
            } else {
                calificationRepo.save(calification);
            }
        }
    }

    public CalificationModel createCalification(CalificationModel calification) {
        return calificationRepo.save(calification);
    }

    public CalificationModel updateCalification(CalificationModel calification) {
        return calificationRepo.save(calification);
    }

    public void deleteCalification(int id) {
        calificationRepo.deleteById(id);
    }
}
