package cnpm.doan.api;

import cnpm.doan.domain.*;
import cnpm.doan.entity.MemberProject;
import cnpm.doan.entity.Project;
import cnpm.doan.entity.Task;
import cnpm.doan.entity.User;
import cnpm.doan.repository.MemberProjectRepository;
import cnpm.doan.repository.TaskRepository;
import cnpm.doan.service.ProjectService;
import cnpm.doan.service.UserService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController()
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private MemberProjectRepository memberProjectRepository;
    @Autowired
    private TaskRepository taskRepository;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/projects")
    public ResponseEntity<?> getAllProject() {
        List<GetAllProjectDomain> projects = projectService.getAllProject();
        if (projects == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.DATA_NOT_EXIST.getDetail(), false));
        }
        if (projects.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), true));
        }
        return ResponseEntity.ok(new ResponeDomain(projects, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/project")
    public ResponseEntity<?> getProjectByUsername(@RequestParam("user_id") int userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), false));
        }
        List<ProjectByUserIdDomain> projects = projectService.getProjectByUserId(userId);
        if (projects.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), true));
        }
        if (projects == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.DATA_NOT_EXIST.getDetail(), true));
        }
        return ResponseEntity.ok(new ResponeDomain(projects, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
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
        try {
            projectService.deleteProject(idProject);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/project/assign_user")
    public ResponseEntity<?> assignUserToProject(@RequestParam("id_project") int idProject, @RequestParam("id_user") String userIds) {
        List<User> users = Arrays.stream(userIds.split(",")).map(id -> userService.findById(Integer.valueOf(id))).collect(Collectors.toList());
        Project project = projectService.findProjectById(idProject);
        if(project == null){
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_PROJECT_ID.getDetail(), HTTPStatus.fail));
        }
        for (User user : users) {
            if (user == null) {
                return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), HTTPStatus.fail));
            }
            if (user.getRoles().getRoleName().equals("ROLE_MANAGER") || user.getRoles().getRoleName().equals("ROLE_ADMIN")) {
                return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), HTTPStatus.fail));
            }
            MemberProject existedUser = memberProjectRepository.findMemberProjectByUserIdAndProjectId(user.getId(), project.getId());
            if (existedUser != null) {
                continue;
            }
            MemberProject memberProject = new MemberProject();
            memberProject.setProject(project);
            memberProject.setUser(user);
            memberProjectRepository.save(memberProject);
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
    @PostMapping("/test")
    public ResponseEntity<?> deleteAllProjectMember(){
        memberProjectRepository.deleteAll();
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
}
