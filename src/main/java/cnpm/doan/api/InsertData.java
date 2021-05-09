package cnpm.doan.api;

import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.entity.*;
import cnpm.doan.repository.MemberProjectRepository;
import cnpm.doan.repository.ProjectRepository;
import cnpm.doan.repository.RoleRepository;
import cnpm.doan.repository.UserRepository;
import cnpm.doan.service.DepartmentService;
import cnpm.doan.service.UserService;
import cnpm.doan.util.DatetimeUtils;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class InsertData {
    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberProjectRepository memberProjectRepository;

    @PostMapping("/insert-data-user")
    public ResponseEntity<?> insertData() {
        userRepository.deleteAll();
        List<Department> departments = departmentService.findAllDepartment();
        List<Role> roles = roleRepository.findAll();
        String password = new BCryptPasswordEncoder().encode("1");
        List<User> users = Arrays.asList(
//                1 ROLE_USER
//                2 ROLE_ADMIN
//                3 ROLE_MANAGER
//                String name, String password, String email, String address, String gender, Role roles, Department department
                new User("Hoàng Trọng Kiên", password, "htkien2511@gmail.com", "Huế", "Nam", roles.get(1), null),
                new User("Nguyễn Thị Hồng", password, "nguyenhong19999@gmail.com", "Quảng Bình", "Nữ", roles.get(0), departments.get(0)),
                new User("Phan Thành Bình", password, "phanbinh2671999@gmail.com", "Quảng Nam", "Nam", roles.get(0), departments.get(1)),
                new User("Phan Gia Sang", password, "sangphan297@gmail.com", "Huế", "Nam", roles.get(0), departments.get(2)),
                new User("Phan Trọng Đức", password, "trongduc.iter@gmail.com", "Phú Yên", "Nam", roles.get(0), departments.get(1)),
                new User("Hoàng Trọng Kiên1", password, "htkien2511+1@gmail.com", "Huế", "Nam", roles.get(0), departments.get(3)),
                new User("Nguyễn Thị Hồng1", password, "nguyenhong19999+1@gmail.com", "Quảng Bình", "Nữ", roles.get(2), departments.get(4)),
                new User("Phan Thành Bình1", password, "phanbinh2671999+1@gmail.com", "Quảng Nam", "Nam", roles.get(2), departments.get(0)),
                new User("Phan Gia Sang1", password, "sangphan297+1@gmail.com", "Huế", "Nam", roles.get(2), departments.get(1)),
                new User("Phan Trọng Đức1", password, "trongduc.iter+1@gmail.com", "Phú Yên", "Nam", roles.get(2), departments.get(2)),
                new User("Hoàng Trọng Kiên2", password, "htkien2511+2@gmail.com", "Huế", "Nam", roles.get(0), departments.get(3)),
                new User("Nguyễn Thị Hồng2", password, "nguyenhong19999+2@gmail.com", "Quảng Bình", "Nữ", roles.get(0), departments.get(5)),
                new User("Phan Thành Bình2", password, "phanbinh2671999+2@gmail.com", "Quảng Nam", "Nam", roles.get(0), departments.get(1)),
                new User("Phan Gia Sang2", password, "sangphan297+2@gmail.com", "Huế", "Nam", roles.get(0), departments.get(2)),
                new User("Phan Trọng Đức2", password, "trongduc.iter+2@gmail.com", "Phú Yên", "Nam", roles.get(0), departments.get(3))
        );
        userRepository.saveAll(users);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PostMapping("/insert-data-project")
    public ResponseEntity<?> insertDataProject() {
        projectRepository.deleteAll();
        String date = "2021/04/08";
        String pattern = DatetimeUtils.YYYYMMDD;
        List<User> manager = userRepository.findUserByRoleName("ROLE_MANAGER");
//        String title, String description, Date dueDate, User manager
        List<Project> projects = Arrays.asList(
                new Project("Intern management", "Backend: Java ; Frontend: React; Database: Postgres", DatetimeUtils.convertStringToDateOrNull(date, pattern), new Date(), manager.get(1)),
                new Project("Fashion shop management", "Backend: Java ; Frontend: React; Database: Postgres", DatetimeUtils.convertStringToDateOrNull(date, pattern), new Date(), manager.get(0)),
                new Project("Library management", "Backend: Java ; Frontend: React; Database: Postgres", DatetimeUtils.convertStringToDateOrNull(date, pattern), new Date(), manager.get(1)),
                new Project("Student management", "Backend: Java ; Frontend: React; Database: Postgres", DatetimeUtils.convertStringToDateOrNull(date, pattern), new Date(), manager.get(0)),
                new Project("Coffee shop management", "Backend: Java ; Frontend: React; Database: Postgres", DatetimeUtils.convertStringToDateOrNull(date, pattern), new Date(), manager.get(2)),
                new Project("News management", "Backend: Java ; Frontend: React; Database: Postgres", DatetimeUtils.convertStringToDateOrNull(date, pattern), new Date(), manager.get(1))
        );
        projectRepository.saveAll(projects);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PostMapping("/insert-data-project-member")
    public ResponseEntity<?> insertDataProjectMember() {
        memberProjectRepository.deleteAll();

        List<User> users = userRepository.findUserByRoleName("ROLE_USER");
        List<Project> projects = projectRepository.findAll();
//        String title, String description, Date dueDate, User manager
        List<MemberProject> projects_member = Arrays.asList(
                new MemberProject(users.get(0), projects.get(1)),
                new MemberProject(users.get(0), projects.get(2)),
                new MemberProject(users.get(1), projects.get(1)),
                new MemberProject(users.get(1), projects.get(2)),
                new MemberProject(users.get(2), projects.get(1)),
                new MemberProject(users.get(2), projects.get(2))
        );
        memberProjectRepository.saveAll(projects_member);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PostMapping("/set-date")
    public ResponseEntity<?> updateDate() {
        List<Project> projects = projectRepository.
                findAll().stream().filter(t -> t.getStartDate() == null).map(t -> {
            t.setStartDate(new Date());
            return t;
        }).collect(Collectors.toList());
        projectRepository.saveAll(projects);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PostMapping("/pass")
    public ResponseEntity<?> updaepass(@RequestParam("user_id") int userId) {
        User user = userRepository.findById(userId).orElse(null);
        user.setPassword(new BCryptPasswordEncoder().encode("1"));
        userRepository.save(user);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }
}
