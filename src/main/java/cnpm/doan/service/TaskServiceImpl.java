package cnpm.doan.service;

import cnpm.doan.entity.Task;
import cnpm.doan.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> findAllTaskByProjectId(long projectId) {
        return taskRepository.findAllByProjectId(projectId);
    }
}
