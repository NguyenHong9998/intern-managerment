package cnpm.doan.service;

import cnpm.doan.domain.TaskDomain;
import cnpm.doan.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAllTaskByProjectId(long projectId);

    List<TaskDomain> getAllTask(long projectId);

    void save(Task task);
}
