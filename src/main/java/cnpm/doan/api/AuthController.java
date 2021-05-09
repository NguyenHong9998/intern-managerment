package cnpm.doan.api;


import cnpm.doan.domain.Account;
import cnpm.doan.domain.ChangePasswordRequest;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.UserWithToken;
import cnpm.doan.entity.User;
import cnpm.doan.security.JwtUtil;
import cnpm.doan.security.UserPrincipal;
import cnpm.doan.service.UserService;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@ModelAttribute Account account) {
        User checkUser = userService.findUserByEmail(account.getEmail());
        UserPrincipal userPrincipal = userService.findByUsername(account.getEmail());
        if (account == null || !new BCryptPasswordEncoder().matches(account.getPassword(), userPrincipal.getPassword())) {
            return ResponseEntity.ok(new ResponeDomain(Message.USER_NOT_FOUND.getDetail(), HTTPStatus.fail));
        }
        if (checkUser != null && checkUser.getRoles() == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.WAITING_ACCOUNT.getDetail(), false));
        }
        String token = jwtUtil.generateToken(userPrincipal);
        User user = userService.findUserByEmail(account.getEmail());
        UserWithToken userWithToken = new UserWithToken(user, token);
        return ResponseEntity.ok(new ResponeDomain(userWithToken, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/change-pass")
    public ResponseEntity<?> changePass(@RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = userService.findById(jwtUtil.getCurrentUser().getUserId());
        if (!new BCryptPasswordEncoder().matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_OLD_PASS.getDetail(), false));
        }
        user.setPassword(new BCryptPasswordEncoder().encode(changePasswordRequest.getNewPassword()));
        userService.createUser(user);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @GetMapping(value = "/")
    public String home() {
        return "Không có gì ngoài sự xinh đẹp của bạn Hồng :)))))";
    }
}
