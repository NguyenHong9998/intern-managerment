package cnpm.doan.api;

import cnpm.doan.domain.ManagerInforDomain;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.UserDomain;
import cnpm.doan.entity.User;
import cnpm.doan.service.UserService;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class ManagerController {
    @Autowired
    private UserService userService;

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
        List<User> managers = userService.findUserByRoleName("ROLE_MANAGER");
        if (managers.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), Message.SUCCESSFUlLY.getDetail(), true));
        }
        List<UserDomain> managerInforDomains = managers.stream().map(t -> new UserDomain(t)).collect(Collectors.toList());
        return ResponseEntity.ok(new ResponeDomain(managerInforDomains, Message.SUCCESSFUlLY.getDetail(), true));
    }
}
