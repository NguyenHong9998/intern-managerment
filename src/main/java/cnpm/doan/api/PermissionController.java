package cnpm.doan.api;

import cnpm.doan.domain.PermissionManagerDomain;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.entity.PermissionEntity;
import cnpm.doan.repository.PermissionRepository;
import cnpm.doan.service.PermissionService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @GetMapping("/permissions")
    public ResponseEntity<?> getAllPermission() {
        List<PermissionEntity> permissions = permissionService.getAllPermission();
        return ResponseEntity.ok(new ResponeDomain(permissions, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PostMapping("/permission/add")
    public ResponseEntity<?> addPermssion(@RequestBody PermissionManagerDomain permissionManagerDomain) {
        try {
            permissionService.addPermissionOfManager(permissionManagerDomain);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), HTTPStatus.fail));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));

    }
}

