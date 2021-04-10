package cnpm.doan.service;

import cnpm.doan.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAllTaskByProjectId(long projectId);
}
