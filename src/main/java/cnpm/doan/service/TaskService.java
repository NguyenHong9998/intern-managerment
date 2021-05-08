package cnpm.doan.service;

import cnpm.doan.domain.TaskDomain;
import cnpm.doan.domain.TaskRequest;
import cnpm.doan.domain.TaskUpdateRequest;
import cnpm.doan.entity.Task;
import cnpm.doan.util.CustormException;

import java.util.List;

public interface TaskService {
    List<Task> findAllTaskByProjectId(int projectId);

    List<TaskDomain> getAllTask(int projectId) throws CustormException;

    void save(TaskRequest task) throws CustormException;

    void deleteTask(int taskId) throws CustormException;

    void update(TaskUpdateRequest TaskUpdateRequest) throws CustormException;
}
