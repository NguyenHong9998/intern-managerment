package cnpm.doan.api;

import cnpm.doan.entity.Task;
import cnpm.doan.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/task/project")
    public ResponseEntity<?> getAllTaskByProjectId(@RequestParam("project_id") long projectId) {
        List<Task> result = taskService.findAllTaskByProjectId(projectId);
        return ResponseEntity.ok(result);
    }
}
