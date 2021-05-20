package cnpm.doan.api;

import cnpm.doan.domain.*;
import cnpm.doan.entity.LeadPermission;
import cnpm.doan.entity.Role;
import cnpm.doan.entity.User;
import cnpm.doan.repository.LeaderPermissionRepository;
import cnpm.doan.service.LeaderPermissionService;
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
    @Autowired
    private LeaderPermissionRepository leaderPermissionRepository;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/managers")
    public ResponseEntity<?> getAllManager() {
        List<User> managers = userService.findAll();
        if (managers.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), Message.SUCCESSFUlLY.getDetail(), true));
        }
        List<ManagerDomain> managerInforDomains = managers.stream().filter(t -> {
            if (t.getRoles() == null) {
                return false;
            }
            if (t.getRoles().getRoleName().equals("ROLE_MANAGER")) {
                return true;
            }
            return false;
        }).map(t -> {
            List<PermissionDomain> leadPermissions = leaderPermissionRepository.findAllByUserId(t.getId())
                    .stream().map(x -> {
                        PermissionDomain permissionDomain = new PermissionDomain();
                        permissionDomain.setId(x.getId());
                        permissionDomain.setName(x.getPermission().getName());
                        return permissionDomain;
                    }).collect(Collectors.toList());
            ManagerDomain managerDomain = new ManagerDomain(t, leadPermissions);
            return managerDomain;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(new ResponeDomain(managerInforDomains, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
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
