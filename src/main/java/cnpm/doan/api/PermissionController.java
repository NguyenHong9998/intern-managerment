package cnpm.doan.api;

import cnpm.doan.domain.PermissionDomain;
import cnpm.doan.domain.PermissionManagerDomain;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.entity.PermissionEntity;
import cnpm.doan.service.PermissionService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin
@Controller
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/permissions")
    public ResponseEntity<?> getAllPermission() {
        List<PermissionEntity> permissions = permissionService.getAllPermission();
        return ResponseEntity.ok(new ResponeDomain(permissions, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/permission/add")
    public ResponseEntity<?> addPermssion(@RequestBody PermissionManagerDomain permissionManagerDomain) {
        try {
            permissionService.addPermissionOfManager(permissionManagerDomain);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), HTTPStatus.fail));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));

    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/permission")
    public ResponseEntity<?> addPermssion(@RequestParam("manager_id") int managerId) {
        List<PermissionDomain> permissionEntities = new ArrayList<>();
        try {
            permissionEntities = permissionService.getPermissionOfManager(managerId);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), HTTPStatus.fail));
        }
        if (CollectionUtils.isEmpty(permissionEntities)) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), HTTPStatus.success));
        }
        return ResponseEntity.ok(new ResponeDomain(permissionEntities, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));

    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/permission/edit")
    public ResponseEntity<?> editPermssion(@RequestBody PermissionManagerDomain permissionManagerDomain) {
        try {
            permissionService.editPermissonOfManager(permissionManagerDomain);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), HTTPStatus.fail));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));

    }
}

