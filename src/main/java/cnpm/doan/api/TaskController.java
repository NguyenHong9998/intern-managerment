package cnpm.doan.api;

import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.TaskDomain;
import cnpm.doan.entity.Task;
import cnpm.doan.service.TaskService;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/task/project")
    public ResponseEntity<?> getAllTaskByProjectId(@RequestParam("project_id") long projectId) {
        List<TaskDomain> domain = taskService.getAllTask(projectId);
        if(domain.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
        }
        return ResponseEntity.ok(new ResponeDomain(domain, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
//
//    @PostMapping("/task/create")
//    public ResponseEntity<?> createTask(@ModelAttribute Task task) {
//
//    }

}
