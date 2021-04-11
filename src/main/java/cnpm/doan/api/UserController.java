package cnpm.doan.api;

import cnpm.doan.domain.RegisterAccount;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.UserDomain;
import cnpm.doan.entity.User;
import cnpm.doan.service.RoleService;
import cnpm.doan.service.UserService;
import cnpm.doan.util.HTTPStatus;
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
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity getUser() {
        List<User> users = userService.findUserByRoleName("ROLE_USER");
        List<UserDomain> result = users.stream().map(t -> new UserDomain(t))
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
        return ResponseEntity.ok(new ResponeDomain(account.getEmail(), Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PostMapping("/user_profile")
    public ResponseEntity<?> getUserProfile(@RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.USER_NOT_FOUND.getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(user, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @GetMapping("/user/waiting_user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAccountWaiting() {
        List<User> getUserWaitting = userService.findWaittingUser();
        return ResponseEntity.ok(getUserWaitting);
    }

    @PutMapping("user/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam("id_user") int idUser) {
        userService.deleteUser(idUser);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
}
