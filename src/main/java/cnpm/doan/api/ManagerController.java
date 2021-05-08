package cnpm.doan.api;

import cnpm.doan.domain.*;
import cnpm.doan.entity.Role;
import cnpm.doan.entity.User;
import cnpm.doan.service.RoleService;
import cnpm.doan.service.UserService;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class ManagerController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

//    @PostMapping("/manager_profile")
//    public ResponseEntity<?> getManagerProfile(@RequestParam("email") String email) {
//        User manager = userService.findUserByEmail(email);
//        if (manager == null) {
//            return ResponseEntity.ok(new ResponeDomain(Message.USER_NOT_FOUND.getDetail(), false));
//        }
//        UserDomain userDomain = new UserDomain(manager);
//        return ResponseEntity.ok(new ResponeDomain(userDomain, Message.SUCCESSFUlLY.getDetail(), true));
//    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/managers")
    public ResponseEntity<?> getAllManager() {
        List<User> managers = userService.findAll();
        if (managers.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), Message.SUCCESSFUlLY.getDetail(), true));
        }
        List<UserDomain> managerInforDomains = managers.stream().filter(t->t.getRoles().getRoleName().equals("ROLE_MANAGE")).map(t -> new UserDomain(t)).collect(Collectors.toList());
        return ResponseEntity.ok(new ResponeDomain(managerInforDomains, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/manager/add")
    public ResponseEntity<?> addAllManager(@ModelAttribute RegisterAccount account) {
        User user = userService.findUserByEmail(account.getEmail());
        if (user != null) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMAIL_EXISTED.getDetail(), false));
        }
        User newUser = new User();
        newUser.setName(account.getName());
        newUser.setEmail(account.getEmail());
        newUser.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
        Role role = roleService.findAll().stream().filter(t -> t.getRoleName().equals("ROLE_MANAGER")).findFirst().get();
        newUser.setRoles(role);
        userService.createUser(newUser);
        WaitingUser response = new WaitingUser(newUser.getName(), newUser.getEmail(), newUser.getId());
        return ResponseEntity.ok(new ResponeDomain(response, Message.SUCCESSFUlLY.getDetail(), true));
    }
}
