package cnpm.doan.api;

import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.entity.PermissionEntity;
import cnpm.doan.repository.PermissionRepository;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PermissionController {
    @Autowired
    PermissionRepository permissionRepository;

    @GetMapping("/permissions")
    public ResponseEntity<?> getAllPermission() {
        List<PermissionEntity> permissions = permissionRepository.findAll();
        return ResponseEntity.ok(new ResponeDomain(permissions, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
}

