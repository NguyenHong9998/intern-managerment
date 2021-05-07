package cnpm.doan.api;

import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.entity.Department;
import cnpm.doan.service.DepartmentService;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("departments")
    public ResponseEntity<?> getAllDepartment() {
        List<Department> departments = departmentService.findAllDepartment();
        return ResponseEntity.ok(new ResponeDomain(departments, Message.SUCCESSFUlLY.getDetail(), true));
    }
}
