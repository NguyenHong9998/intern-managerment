package cnpm.doan.api;

import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.TaskDomain;
import cnpm.doan.domain.TaskRequest;
import cnpm.doan.entity.User;
import cnpm.doan.service.*;
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

@CrossOrigin(maxAge = 3600)
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MemberProjectService memberProjectService;

    @Autowired
    private MemberTaskService memberTaskService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')")
    @GetMapping("/task/project")
    public ResponseEntity<?> getAllTaskByProjectId(@RequestParam("project_id") int projectId) {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1");
        List<TaskDomain> domain = taskService.getAllTask(projectId);
        domain.forEach(System.out::println);
        if (domain.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), HTTPStatus.success));
        }
        return ResponseEntity.ok(new ResponeDomain(domain, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_USER')")
    @PostMapping("/task/create")
    public ResponseEntity<?> createTask(@ModelAttribute TaskRequest taskRequest) {
        try {
            taskService.save(taskRequest);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(null, e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(null, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/task/delete")
    public ResponseEntity<?> deleteTask(@RequestParam("id_task") int idTask) {
        try {
            taskService.deleteTask(idTask);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(null, Message.SUCCESSFUlLY.getDetail(), true));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    @PostMapping("/task/assign_user")
    public ResponseEntity<?> assignUserToTask(@RequestParam("id_task") int idTask, @RequestParam("id_user") String userIds) {
        List<User> users = Arrays.stream(userIds.split(",")).map(id -> userService.findById(Integer.valueOf(id))).collect(Collectors.toList());
        try {
            memberTaskService.assignUserToTask(idTask, users);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(null, e.getErrorType().getDetail(), true));
        }
        return ResponseEntity.ok(new ResponeDomain(null, Message.SUCCESSFUlLY.getDetail(), true));

    }


}
