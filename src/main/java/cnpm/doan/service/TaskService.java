package cnpm.doan.service;

import cnpm.doan.domain.TaskDomain;
import cnpm.doan.domain.TaskRequest;
import cnpm.doan.entity.Task;
import cnpm.doan.util.CustormException;

import java.util.List;

public interface TaskService {
    List<Task> findAllTaskByProjectId(int projectId);

    List<TaskDomain> getAllTask(int projectId);

    void save(TaskRequest task) throws CustormException;
}
