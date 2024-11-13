import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.api.backend.services.UserService;
import com.api.backend.security.JwtService;
import com.api.backend.services.UserXGroupService;
import java.util.List;
import com.api.backend.model.StudentWithGroupModel;

@RestController
public class UserXGroupControl {

    @Autowired
    private UserXGroupService userXGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/student-groups")
    public ResponseEntity<?> getStudentsWithGroups(@CookieValue(name = "token") String token) {
        try {
            if (jwtService.ValidateTokenAdmin(token)) {
                List<StudentWithGroupModel> studentsWithGroups = userXGroupService.getAllStudentsWithGroup();
                return ResponseEntity.ok(studentsWithGroups);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los estudiantes con grupo");
        }
    }

    @PutMapping("/updateGroupById")
    public ResponseEntity<Boolean> updateGroupById(@CookieValue(name = "token") String token, @RequestHeader("StudentId")int studentId,@RequestHeader("GroupId")int groupId) {
        if (jwtService.ValidateTokenAdmin(token)) {
            userXGroupService.assignOrUpdateGroup(studentId, groupId);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}