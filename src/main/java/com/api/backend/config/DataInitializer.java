package com.api.backend.config;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.backend.model.ClassModel;
import com.api.backend.model.GroupModel;
import com.api.backend.model.RolModel;
import com.api.backend.model.SubjectModel;
import com.api.backend.model.UserModel;
import com.api.backend.model.UserxGroupModel;
import com.api.backend.services.ClassService;
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

    @Autowired
    private ClassService classService;

    private static final String DEFAULT_PICTURE = "https://i.gifer.com/XOsX.gif";

    Boolean si=false;
    @PostConstruct
    public void init() {
        if (si){
        initializeRoles();
        initializeUsers();
        initializeGroups();
        initializeStudents();
        initializeSubjects();
        initializeClasses();
}
    }

    private void initializeRoles() {
        if (rolService.obtainRolList().isEmpty()) {
            String[] roles = { "Student", "Parent", "Teacher", "Admin" };
            boolean[] assignable = { true, true, false, false };
            for (int i = 0; i < roles.length; i++) {
                rolService.createRol(new RolModel(0, assignable[i], roles[i]));
            }
        }
    }

    private void initializeUsers() {
        if (userService.obtainUserList().isEmpty()) {
            RolModel adminRole = getRoleByName("Admin");

            String[][] admins = {
                { "Santiago", "Trespalacios", "2003-02-09", "Masculino", "Calle 123", "santiagot3p@gmail.com" },
                { "David", "Gomez", "2004-09-16", "Masculino", "Calle UDea", "david.gomez.ingeniero@gmail.com" }
            };

            for (String[] admin : admins) {
                createUser(admin[0], admin[1], admin[2], admin[3], admin[4], admin[5], "CC", "1234567890", adminRole);
            }

            String[] nombres = { "Albus", "Minerva", "Severus", "Pomona", "Filius", "Sybill", "Rubeus" };
            String[] apellidos = { "Dumbledore", "McGonagall", "Snape", "Sprout", "Flitwick", "Trelawney", "Hagrid" };
            String[] fechasNacimiento = { "1881-08-30", "1935-10-04", "1960-01-09", "1930-05-15", "1958-10-17", "1950-03-09", "1928-12-06" };
            String[] generos = { "Masculino", "Femenino", "Masculino", "Femenino", "Masculino", "Femenino", "Masculino" };

            RolModel teacherRole = getRoleByName("Teacher");
            for (int i = 0; i < nombres.length; i++) {
                createUser(nombres[i], apellidos[i], fechasNacimiento[i], generos[i],
                        "Hogwarts Castle " + (i + 1), nombres[i].toLowerCase() + "." + apellidos[i].toLowerCase() + "@hogwarts.com",
                        "CC", "" + (1234567890 + i), teacherRole);
            }
        }
    }

    private void initializeGroups() {
        if (groupService.obtainGroupList().isEmpty()) {
            String[] variants = { "G", "S", "H", "R" };
            for (int i = 1; i <= 7; i++) {
                for (String variant : variants) {
                    groupService.createGroup(new GroupModel(0, i, variant));
                }
            }
        }
    }

        private void initializeStudents() {
        if (!userService.obtainUserList().isEmpty() && !groupService.obtainGroupList().isEmpty()) {
            String[] nombres = { "Harry", "Hermione", "Ron", "Ginny", "Neville", "Draco", "Pansy", "Blaise", "Crabbe", "Goyle",
                                 "Luna", "Cho", "Padma", "Terry", "Michael", "Cedric", "Hannah", "Ernie", "Susan", "Justin" };
            String[] apellidos = { "Potter", "Granger", "Weasley", "Weasley", "Longbottom", "Malfoy", "Parkinson", "Zabini",
                                   "Crabbe", "Goyle", "Lovegood", "Chang", "Patil", "Boot", "Corner", "Diggory", "Abbott",
                                   "Macmillan", "Bones", "Finch-Fletchley" };
            String[] casas = { "Gryffindor", "Slytherin", "Ravenclaw", "Hufflepuff" };
            int[] años = { 1, 2, 3, 4, 5, 6, 7 };

            RolModel studentRole = getRoleByName("Student");
            List<GroupModel> allGroups = groupService.obtainGroupList();

            for (int i = 0; i < nombres.length; i++) {
                final int index = i;
                createUser(nombres[i], apellidos[i], "2005-09-01", (i % 2 == 0) ? "Masculino" : "Femenino",
                        "Hogwarts Castle " + (i + 1), nombres[i].toLowerCase() + "." + apellidos[i].toLowerCase() + "@hogwarts.com",
                        "CC", "" + (1234567890 + i), studentRole);
                        GroupModel group = allGroups.stream()
                        .filter(g -> g.getVariant().equals(String.valueOf(casas[index    % casas.length].charAt(0)))
                                && g.getGrade() == años[index % años.length])
                                .findFirst().orElse(null);

                if (group != null) {
                    userXGroupService.createUserXGroup(new UserxGroupModel(0, userService.obtainUserByEmail(nombres[i].toLowerCase() + "." + apellidos[i].toLowerCase() + "@hogwarts.com"), group));
                }
            }
        }
}
    
        

        private void initializeSubjects() {
                if (subjectService.obtainSubjectList().isEmpty()) {
                    String[][] subjects = {
                        { "Dark Arts", "Study of dark creatures, curses, and spells", "https://img.pikbest.com/wp/202347/fog-forest-enchanted-nightfall-dark-tree-silhouettes-amid-mysterious-glow-and-3d-digital-art_9767148.jpg!w700wp" },
                        {"Potions", "Learning potion-making techniques and ingredients","https://cdn.pixabay.com/photo/2023/02/23/22/45/ai-generated-7809734_1280.jpg"},
                        {"Herbology", "Study of magical plants and fungi","https://www.gob.mx/cms/uploads/image/file/612568/herbolaria.jpeg"},
                        {"Defense Against the Dark Arts", "Protection against dark magic and creatures","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGFCZZFdnmejz86-ROteZbZgU22VK9-Vt2wyncLMBtEQix6rz5ZaF0J5RFKRDHCrtTXwY&usqp=CAU"},
                        {"Astronomy", "Study of stars, planets, and magical constellations","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSCUUeAy0U7iC2fZEoa6a3sASmRXLo5dswl6Q&s"},
                        {"History of Magic", "Exploration of magical history and significant events","https://m.media-amazon.com/images/I/91IOmSR775L._AC_UF1000,1000_QL80_.jpg"},
                        {"Divination", "Learning techniques to foresee the future","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQSz22ZOEcmDOrc5IiYn00Ni1UDOqbvreV_Q&s"},
                        {"Care of Magical Creatures", "Handling and caring for magical creatures","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3llsRTRpRtfoEeywMvFXOh1jDOk1oC37k8Q&s"},
                        {"Ancient Runes", "Study of magical runes and symbols","https://thumbs.dreamstime.com/b/ancient-scandinavian-ornament-shield-viking-scandinavian-runes-isolated-black-vector-illustration-93015755.jpg"},
                        {"Arithmancy", "Magical interpretation of numbers and their meanings","https://static1.srcdn.com/wordpress/wp-content/uploads/2024/06/arithmancy-worksheets-harry-potter.jpg"},
                      
                    };
        
                    for (String[] subject : subjects) {
                        subjectService.createSubject(new SubjectModel(0, subject[0], subject[1], subject[2]));
                    }
                }
        }
        public void initializeClasses() {
                // Obtener datos existentes de las tablas
                List<GroupModel> allGroups = groupService.obtainGroupList();
                List<UserModel> allTeachers = userService.obtainTeacherList(); // Solo los usuarios con rol de maestro
                List<SubjectModel> allSubjects = subjectService.obtainSubjectList();
        
                // Validar que haya suficientes datos
                if (allGroups.isEmpty() || allTeachers.isEmpty() || allSubjects.isEmpty()) {
                    System.out.println("No hay suficientes datos para inicializar las clases.");
                    return;
                }
        
                // Crear horarios ficticios
                String[] schedules = {
                    "Lunes 8-10",
                    "Lunes 10-12",
                    "Martes 8-10",
                    "Martes 10-12",
                    "Miércoles 08-10",
                    "Miércoles 10-12",
                    "Jueves 8-10",
                    "Jueves 10-12",
                    "Viernes 8-10",
                    "Viernes 10-12"
                };
        
                // Crear 10 clases
                for (int i = 0; i < 10; i++) {
                    GroupModel group = allGroups.get(i % allGroups.size());
                    UserModel teacher = allTeachers.get(i % allTeachers.size());
                    SubjectModel subject = allSubjects.get(i % allSubjects.size());
                    String schedule = schedules[i % schedules.length];
        
                    ClassModel newClass = new ClassModel();
                    newClass.setGroup(group);
                    newClass.setTeacher(teacher);
                    newClass.setSubject(subject);
                    newClass.setSchedule(schedule);
                    classService.createClass(newClass);
                }
        
                System.out.println("Se inicializaron 10 clases correctamente.");
            }

            
            private RolModel getRoleByName(String name) {
                return rolService.obtainRolList().stream()
                        .filter(rol -> rol.getName().equals(name))
                        .findFirst().orElse(null);
            }
        
            private void createUser(String name, String lastName, String birthday, String gender, String address, String email,
                                    String documentType, String documentNumber, RolModel role) {
                userService.createUser(new UserModel(0, name, lastName, Date.valueOf(birthday), gender, address, "123456789", email,
                        documentType, documentNumber, role, DEFAULT_PICTURE));
            }

}

