package cnpm.doan.api;

import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.TaskDomain;
import cnpm.doan.domain.TaskRequest;
import cnpm.doan.service.ProjectService;
import cnpm.doan.service.TaskService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/task/project")
    public ResponseEntity<?> getAllTaskByProjectId(@RequestParam("project_id") int projectId) {
        List<TaskDomain> domain = taskService.getAllTask(projectId);
        if (domain.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), HTTPStatus.success));
        }
        return ResponseEntity.ok(new ResponeDomain(domain, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PostMapping("/task/create")
    public ResponseEntity<?> createTask(@ModelAttribute TaskRequest taskRequest) {
        try {
            taskService.save(taskRequest);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(null, e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(null, Message.SUCCESSFUlLY.getDetail(), true));
    }

    public ResponseEntity<?> deleteTask() {
        return null;
    }


}
