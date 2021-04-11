package cnpm.doan.service;

import cnpm.doan.domain.TaskDomain;
import cnpm.doan.entity.Task;
import cnpm.doan.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> findAllTaskByProjectId(long projectId) {
        return taskRepository.findAllByProjectId(projectId);
    }

    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Override
    public List<TaskDomain> getAllTask(long projectId) {
        List<Task> findAllTask = findAllTaskByProjectId(projectId);
        List<TaskDomain> result = findAllTask.stream().map(task -> {
            TaskDomain taskDomain = new TaskDomain();
            taskDomain.setTaskId(task.getId());
            taskDomain.setDescription(task.getDescription());
            taskDomain.setTitle(task.getTitle());
            taskDomain.setDifficulty(task.getDifficulty().getName());
            taskDomain.setPoint(String.format("%.2s", task.getPoint()));
            taskDomain.setProjectName(task.getProject().getTitle());
            taskDomain.setIsDone(task.isDone());
            return taskDomain;
        }).collect(Collectors.toList());
        return result;
    }
}
