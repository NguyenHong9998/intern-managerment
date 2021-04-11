package cnpm.doan.api;

import cnpm.doan.domain.ManagerInforDomain;
import cnpm.doan.domain.ProjectDomain;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.entity.Project;
import cnpm.doan.entity.User;
import cnpm.doan.service.ProjectService;
import cnpm.doan.service.UserService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController()
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/projects")
    public ResponseEntity<?> getAllProject() {
        List<Project> projects = projectService.getAllProject();
        if (projects == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.DATA_NOT_EXIST.getDetail(), false));
        }
        if (projects.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), true));
        }
        return ResponseEntity.ok(new ResponeDomain(projects, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @GetMapping("/project")
    public ResponseEntity<?> getProjectByUsername(@RequestParam("user_id") long userId) {
        List<Project> projects = projectService.getProjectByUserId(userId);
        if (projects.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), true));
        }
        if (projects == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.DATA_NOT_EXIST.getDetail(), true));
        }
        return ResponseEntity.ok(projects);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/project/create")
    public ResponseEntity<?> createProject(@ModelAttribute ProjectDomain request) {
        User manager = userService.findById(request.getIdOfAdmin());
        if (manager == null || !manager.getRoles().getRoleName().equals("ROLE_MANAGER")) {
            return ResponseEntity.ok(new ResponeDomain(Message.NOT_EXIST_MANAGER.getDetail(), false));
        }
        if (request.getDescription().isEmpty() || request.getDueDate().isEmpty() || request.getTitle().isEmpty()) {
            return ResponseEntity.ok(new ResponeDomain(Message.DATA_NOT_EXIST.getDetail(), false));
        }
        try {
            projectService.saveProject(request);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(request, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @GetMapping("/project/create")
    public ResponseEntity<?> getAllManagerForCreateProject() {
        List<ManagerInforDomain> managers = userService.findUserByRoleName("ROLE_MANAGER")
                .stream().map(u -> new ManagerInforDomain(u.getId(), u.getName(), u.getEmail())).collect(Collectors.toList());
        return ResponseEntity.ok(new ResponeDomain(managers, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/project/delete")
    public ResponseEntity<?> deleteProject(@RequestParam("id_project") int idProject) {
        Project project = projectService.findProjectById(idProject);
        if (project == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.DATA_NOT_EXIST.getDetail(), false));
        }
        projectService.deleteProject(idProject);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }
}
