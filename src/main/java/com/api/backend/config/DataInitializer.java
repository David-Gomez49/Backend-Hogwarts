package com.api.backend.config;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.backend.model.GroupModel;
import com.api.backend.model.RolModel;
import com.api.backend.model.StudentWithGroupModel;
import com.api.backend.model.SubjectModel;
import com.api.backend.model.UserModel;
import com.api.backend.model.UserxGroupModel;
import com.api.backend.services.GroupService;
import com.api.backend.services.RolService;
import com.api.backend.services.SubjectService;
import com.api.backend.services.UserService;
import com.api.backend.services.UserXGroupService;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private RolService rolService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserXGroupService userXGroupService;

    @Autowired
    private SubjectService subjectService;

    @PostConstruct
    public void init() {
        // Verificar y agregar roles si no existen
        if (rolService.obtainRolList().isEmpty()) {
            rolService.createRol(new RolModel(0, true, "Student"));
            rolService.createRol(new RolModel(0, true, "Parent"));
            rolService.createRol(new RolModel(0, false, "Teacher"));
            rolService.createRol(new RolModel(0, false, "Admin"));
        }

        // Crear el usuario solo si no existen usuarios
        if (userService.obtainUserList().isEmpty()) {
            String patolink="https://i.gifer.com/XOsX.gif";
            userService.createUser(new UserModel(
                    0, // ID se genera automáticamente
                    "Santiago",
                    "Trespalacios",
                    Date.valueOf("2003-02-09"),
                    "Masculino",
                    "Calle 123",
                    "123456789",
                    "santiagot3p@gmail.com",
                    "CC",
                    "1234567890",
                    rolService.obtainRolList().stream().filter(rol -> rol.getName().equals("Admin")).findFirst()
                            .orElse(null) // Asignar rol Admin
                    , patolink));

                    userService.createUser(new UserModel(
                    0, // ID se genera automáticamente
                    "David",
                    "Gomez",
                    Date.valueOf("2004-09-16"),
                    "Masculino",
                    "Calle UDea",
                    "123456789",
                    "david.gomez.ingeniero@gmail.com",
                    "CC",
                    "1234567890",
                    rolService.obtainRolList().stream().filter(rol -> rol.getName().equals("Admin")).findFirst()
                            .orElse(null) // Asignar rol Admin
                    , patolink));

            String[] nombres = { "Albus", "Minerva", "Severus", "Pomona", "Filius", "Sybill", "Rubeus" };
            String[] apellidos = { "Dumbledore", "McGonagall", "Snape", "Sprout", "Flitwick", "Trelawney", "Hagrid" };
            String[] fechasNacimiento = { "1881-08-30", "1935-10-04", "1960-01-09", "1930-05-15", "1958-10-17",
                    "1950-03-09", "1928-12-06" };
            String[] generos = { "Masculino", "Femenino", "Masculino", "Femenino", "Masculino", "Femenino",
                    "Masculino" };

            for (int i = 0; i < 7; i++) {
                String email = nombres[i].toLowerCase() + "." + apellidos[i].toLowerCase() + "@hogwarts.com";
                userService.createUser(new UserModel(
                        0, // ID se genera automáticamente
                        nombres[i],
                        apellidos[i],
                        Date.valueOf(fechasNacimiento[i]), // Fecha de nacimiento específica
                        generos[i],
                        "Hogwarts Castle " + (i + 1),
                        "1234567890" + (i + 1),
                        email,
                        "CC",
                        "" + (1234567890 + i),
                        rolService.obtainRolList().stream().filter(rol -> rol.getName().equals("Teacher")).findFirst()
                                .orElse(null), // Asignar rol Teacher
                        "picture"));
            }

            if (groupService.obtainGroupList().isEmpty()) {
                String[] variants = {"G", "S", "H", "R"};
                for (int i = 1; i <= 7; i++) {
                    for (String variant : variants) {
                        groupService.createGroup(new GroupModel(0, i, variant));
                    }
                }
            }
            
            // Define student data
            String[] nombresstd = { "Harry", "Hermione", "Ron", "Ginny", "Neville", // Gryffindor
                    "Draco", "Pansy", "Blaise", "Crabbe", "Goyle", // Slytherin
                    "Luna", "Cho", "Padma", "Terry", "Michael", // Ravenclaw
                    "Cedric", "Hannah", "Ernie", "Susan", "Justin" }; // Hufflepuff
            
            String[] apellidosstd = { "Potter", "Granger", "Weasley", "Weasley", "Longbottom", // Gryffindor
                    "Malfoy", "Parkinson", "Zabini", "Crabbe", "Goyle", // Slytherin
                    "Lovegood", "Chang", "Patil", "Boot", "Corner", // Ravenclaw
                    "Diggory", "Abbott", "Macmillan", "Bones", "Finch-Fletchley" }; // Hufflepuff
            
            String[] casas = { "Gryffindor", "Gryffindor", "Gryffindor", "Gryffindor", "Gryffindor", // Gryffindor
                    "Slytherin", "Slytherin", "Slytherin", "Slytherin", "Slytherin", // Slytherin
                    "Ravenclaw", "Ravenclaw", "Ravenclaw", "Ravenclaw", "Ravenclaw", // Ravenclaw
                    "Hufflepuff", "Hufflepuff", "Hufflepuff", "Hufflepuff", "Hufflepuff" }; // Hufflepuff
            
            int[] años = { 1, 1, 2, 3, 4, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 7, 7, 5, 4 }; // Student years
            
            // Retrieve the "Student" role
            RolModel studentRole = rolService.obtainRolList().stream()
                                             .filter(rol -> "Student".equals(rol.getName()))
                                             .findFirst().orElse(null);
            
            // Retrieve all groups once to avoid redundant calls in the loop
            List<GroupModel> allGroups = groupService.obtainGroupList();
            
            for (int i = 0; i < nombresstd.length; i++) {
                String email = nombresstd[i].toLowerCase() + "." + apellidosstd[i].toLowerCase() + "@hogwarts.com";
                UserModel student = new UserModel(
                        0, // Auto-generated ID
                        nombresstd[i],
                        apellidosstd[i],
                        Date.valueOf("2005-09-01"),
                        (i % 2 == 0) ? "Masculino" : "Femenino",
                        "Hogwarts Castle " + (i + 1),
                        "1234567890" + (i + 1),
                        email,
                        "CC",
                        String.valueOf(1234567890 + i),
                        studentRole,
                        "picture"
                );
            
                userService.createUser(student);
            
                
                String groupVariant = String.valueOf(casas[i].charAt(0)); // First letter of house (G, S, H, R)
                int studentYear = años[i];
            
                GroupModel group = allGroups.stream()
                                            .filter(g -> g.getVariant().equals(groupVariant) && g.getGrade() == studentYear)
                                            .findFirst().orElse(null);
            
                if (group != null) {
                    // Assign the student to the group
                    userXGroupService.createUserXGroup(new UserxGroupModel(0, student, group));
                }
            }
                   
                    

        }

        if (subjectService.obtainSubjectList().isEmpty()) {
                // Crear materias
                subjectService.createSubject(new SubjectModel(0, "Dark Arts", "Study of dark creatures, curses, and spells",""));
                subjectService.createSubject(new SubjectModel(0, "Potions", "Learning potion-making techniques and ingredients",""));
                subjectService.createSubject(new SubjectModel(0, "Herbology", "Study of magical plants and fungi",""));
                subjectService.createSubject(new SubjectModel(0, "Transfiguration", "Transformation of objects and living beings",""));
                subjectService.createSubject(new SubjectModel(0, "Charms", "Casting and understanding various charms",""));
                subjectService.createSubject(new SubjectModel(0, "Defense Against the Dark Arts", "Protection against dark magic and creatures",""));
                subjectService.createSubject(new SubjectModel(0, "Astronomy", "Study of stars, planets, and magical constellations",""));
                subjectService.createSubject(new SubjectModel(0, "History of Magic", "Exploration of magical history and significant events",""));
                subjectService.createSubject(new SubjectModel(0, "Divination", "Learning techniques to foresee the future",""));
                subjectService.createSubject(new SubjectModel(0, "Care of Magical Creatures", "Handling and caring for magical creatures",""));
                subjectService.createSubject(new SubjectModel(0, "Ancient Runes", "Study of magical runes and symbols",""));
                subjectService.createSubject(new SubjectModel(0, "Arithmancy", "Magical interpretation of numbers and their meanings",""));


                        }





    }
}
