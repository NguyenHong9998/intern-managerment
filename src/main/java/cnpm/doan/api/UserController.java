package cnpm.doan.api;

import cnpm.doan.domain.*;
import cnpm.doan.entity.Department;
import cnpm.doan.entity.Task;
import cnpm.doan.entity.User;
import cnpm.doan.repository.MemberTaskRepository;
import cnpm.doan.security.JwtUtil;
import cnpm.doan.security.UserPrincipal;
import cnpm.doan.service.DepartmentService;
import cnpm.doan.service.UserService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private MemberTaskRepository memberTaskRepository;

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity getUser() {
        List<User> users = userService.findAll();
        List<UserDomain> result = users.stream().filter(t -> {
            if (t.getRoles() == null) {
                return false;
            }
            if (t.getRoles().getRoleName().equals("ROLE_USER")) {
                return true;
            }
            return false;
        }).map(t -> new UserDomain(t))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ResponeDomain(result, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute RegisterAccount account) {
        User user = userService.findUserByEmail(account.getEmail());
        if (user != null) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMAIL_EXISTED.getDetail(), false));
        }
        User newUser = new User();
        newUser.setName(account.getName());
        newUser.setEmail(account.getEmail());
        newUser.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
        userService.createUser(newUser);
        WaitingUser response = new WaitingUser(newUser.getName(), newUser.getEmail(), newUser.getId());
        return ResponseEntity.ok(new ResponeDomain(response, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/user_profile")
    public ResponseEntity<?> getUserProfile(@RequestParam("id_user") int userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), false));
        }
        UserDomain userDomain = new UserDomain(user);
        return ResponseEntity.ok(new ResponeDomain(userDomain, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    @PostMapping("/user_profile/edit")
    public ResponseEntity<?> updateUserProfile(@ModelAttribute UserDomain userDomain) {
        User user = userService.findById(userDomain.getId());
        UserPrincipal userPrincipal = jwtUtil.getCurrentUser();
        String role = userPrincipal.getAuthorities().stream().findFirst().get().toString();
        User user1 = userService.findUserByEmail(userDomain.getEmail());
        if (user1 != null && user.getId() != user1.getId()) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMAIL_EXISTED.getDetail(), false));
        }
        if (userDomain.getId() != userPrincipal.getUserId() && !role.equals(("ROLE_ADMIN"))) {
            return ResponseEntity.ok(new ResponeDomain(Message.PERMISION_EDIT_ANOTHER_ACC.getDetail(), false));
        }
        if (user == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), false));
        }
        Department department = departmentService.findById(Integer.valueOf(userDomain.getDepartment()));
        user.setName(userDomain.getName());
        user.setAddress(userDomain.getAddress());
        user.setGender(userDomain.getGender());
        user.setId(userDomain.getId());
        if (!user.getEmail().equals(userDomain.getEmail())
                || (user.getDepartment() == null) && (department != null)
                || (user.getDepartment().getId() != Integer.valueOf(userDomain.getDepartment()))) {
            if (role.equals("ROLE_ADMIN")) {
                user.setDepartment(department);
                user.setEmail(userDomain.getEmail());
                userService.createUser(user);
                return ResponseEntity.ok(new ResponeDomain(new UserDomain(user), Message.SUCCESSFUlLY.getDetail(), true));
            } else {
                return ResponseEntity.ok(new ResponeDomain(Message.CANOT_UPDATE_EMAIL.getDetail(), false));
            }
        }

        userService.createUser(user);
        return ResponseEntity.ok(new ResponeDomain(new UserDomain(user), Message.SUCCESSFUlLY.getDetail(), true));
    }

    @GetMapping("/user/waiting_user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAccountWaiting() {
        List<WaitingUser> getUserWaiting = userService.findWaittingUser();
        if (getUserWaiting.isEmpty()) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), true));
        }
        return ResponseEntity.ok(new ResponeDomain(getUserWaiting, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PutMapping("user/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam("id_user") int idUser) {
        try {
            userService.deleteUser(idUser);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), HTTPStatus.fail));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PutMapping("user/deny")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> denyUsers(@RequestBody @Validated int[] userIds) {
        try {
            userService.denyUser(userIds);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PutMapping("user/accept")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> acceptUsers(@RequestBody @Validated int[] userIds) {
        try {
            userService.acceptUsers(userIds);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @GetMapping("user/result")
    public ResponseEntity<?> getResultOfUser(@RequestParam("user_id") int userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), false));
        }
        List<Task> taskOfUser = memberTaskRepository.findByUserId(userId);
        taskOfUser.stream().forEach(t -> System.out.println("xxxxxxxxxxxxxxx: " + t.getPoint()));
        return ResponseEntity.ok(new ResponeDomain(taskOfUser, Message.SUCCESSFUlLY.getDetail(), true));

    }
}
