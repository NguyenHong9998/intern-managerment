package cnpm.doan.api;

import cnpm.doan.domain.*;
import cnpm.doan.entity.*;
import cnpm.doan.repository.*;
import cnpm.doan.security.JwtUtil;
import cnpm.doan.service.ProjectService;
import cnpm.doan.service.UserService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.DatetimeUtils;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
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
    private JwtUtil jwtUtil;
    @Autowired
    private MemberTaskRepository memberTaskRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
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
        User user = userService.findById(jwtUtil.getCurrentUser().getUserId());

        if (userId != jwtUtil.getCurrentUser().getUserId()
                && user.getRoles().getRoleName().equals("ROLE_USER")) {
            return ResponseEntity.ok(new ResponeDomain(Message.CANNOT_GET_PRO_ANOTHER.getDetail(), false));
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/project/users")
    public ResponseEntity<?> getUsersOfProject(@RequestParam("id_project") int idProject) {
        User user = userService.findById(jwtUtil.getCurrentUser().getUserId());
//        MemberProject memberProject = memberProjectRepository.findMemberProjectByUserIdAndProjectId(jwtUtil.getCurrentUser().getUserId(), idProject);
//        if (memberProject == null && !user.getRoles().getRoleName().equals("ROLE_ADMIN")) {
//            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_USER.getDetail(), true));
//        }
        List<User> users = memberProjectRepository
                .findMemberProjectByProjectId(idProject).stream().map(t -> t.getUser()).collect(Collectors.toList());
        List<UserDomain> result = users.stream().map(
                t -> new UserDomain(t))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ResponeDomain(result, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
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
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    @PostMapping(value = "/project/delete")
    public ResponseEntity<?> deleteProject(@RequestParam("id_project") int idProject) {
        try {
            projectService.deleteProject(idProject);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    @PutMapping("/project/assign_user")
    public ResponseEntity<?> assignUserToProject(@RequestParam("id_project") int idProject, @RequestParam("id_user") String userIds) {
        List<User> newUsers = new ArrayList<>();
        if (userIds.equals("") || userIds == null) {
            newUsers = new ArrayList<>();
        } else {
            newUsers = Arrays.stream(userIds.split(",")).map(id -> userService.findById(Integer.valueOf(id))).collect(Collectors.toList());
        }
        Project project = projectService.findProjectById(idProject);
        if (project == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_PROJECT_ID.getDetail(), HTTPStatus.fail));
        }
        List<MemberProject> memberProjects = memberProjectRepository.findMemberProjectByProjectId(idProject);
        List<Integer> memberIds = memberProjects.stream().map(t -> t.getUser().getId()).collect(Collectors.toList());
        List<Integer> newUserId = newUsers.stream().map(t -> t.getId()).collect(Collectors.toList());
        memberIds.addAll(newUserId);
        List<Integer> diffMem = memberIds.stream().filter(t -> !newUserId.contains(t)).collect(Collectors.toList());
        // delete diff in memproject
        List<MemberProject> memberProjects1 = memberProjects.stream().filter(t -> diffMem.contains(t.getUser().getId())).collect(Collectors.toList());
        memberProjectRepository.deleteAll(memberProjects1);

        // delete diff in task
        List<Integer> tasks = taskRepository.findAll().stream().filter(t -> t.getProject().getId() == idProject).map(t -> t.getId()).collect(Collectors.toList());
        List<MemberTask> memberTasks = memberTaskRepository.findAll().stream().filter(t -> tasks.contains(t.getTask().getId()) && diffMem.contains(t.getUser().getId())).collect(Collectors.toList());
        // delete diff feedback in task
        memberTaskRepository.deleteAll(memberTasks);

        for (User user : newUsers) {
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    @PutMapping("/project/update")
    public ResponseEntity<?> updateProject(@RequestParam("id_project") int idProject, @ModelAttribute ProjectDomain projectDomain) {
        Project project = projectService.findProjectById(idProject);
        if (project == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_PROJECT_ID.getDetail(), HTTPStatus.fail));
        }
        User user = userService.findById(jwtUtil.getCurrentUser().getUserId());
        if (!user.getRoles().getRoleName().equals("ROLE_MANAGER") && !user.getRoles().getRoleName().equals("ROLE_ADMIN")) {
            return ResponseEntity.ok(new ResponeDomain(Message.CANNOT_UPDATE_PROJECT.getDetail(), HTTPStatus.fail));
        }
        User manager = userService.findById(projectDomain.getIdOfAdmin());
        if (manager == null) {
            return ResponseEntity.ok(new ResponeDomain(Message.INVALID_MANGER.getDetail(), HTTPStatus.fail));
        }
        project.setTitle(projectDomain.getTitle());
        project.setDescription(projectDomain.getDescription());
        project.setDueDate(DatetimeUtils.convertStringToDateOrNull(projectDomain.getDueDate(), DatetimeUtils.YYYYMMDD));
        project.setManager(manager);
        projectRepository.save(project);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/project/manager")
    public ResponseEntity<?> getProjectByManagerId(@RequestParam("manager_id") int managerId) {
        List<GetAllProjectDomain> result = projectService.findProjectByManagerId(managerId);
        if (result.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), HTTPStatus.success));
        }
        return ResponseEntity.ok(new ResponeDomain(result, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
}
