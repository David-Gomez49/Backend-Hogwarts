package com.api.backend.model;

public class StudentWithGroupModel {
        private UserModel student;
        private GroupModel group;
    
        public StudentWithGroupModel()
        {

        }

        public StudentWithGroupModel(UserModel student, GroupModel group) {
            this.student = student;
            this.group = group;
        }

        public UserModel getStudent() {
            return student;
        }

        public void setStudent(UserModel student) {
            this.student = student;
        }

        public GroupModel getGroup() {
            return group;
        }

        public void setGroup(GroupModel group) {
            this.group = group;
        }
    
    }
    

