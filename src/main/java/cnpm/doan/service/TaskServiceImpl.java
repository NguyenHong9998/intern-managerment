package cnpm.doan.service;

import cnpm.doan.domain.TaskDomain;
import cnpm.doan.domain.TaskRequest;
import cnpm.doan.entity.Difficulty;
import cnpm.doan.entity.Project;
import cnpm.doan.entity.Task;
import cnpm.doan.repository.DifficultyRepository;
import cnpm.doan.repository.ProjectRepository;
import cnpm.doan.repository.TaskRepository;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.DatetimeUtils;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    DifficultyRepository difficultyRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public List<Task> findAllTaskByProjectId(int projectId) {
        return taskRepository.findAllByProjectId(projectId);
    }


    @Override
    public void save(TaskRequest taskRequest) throws CustormException {
        Task task = new Task();
        task.setDescription(taskRequest.getDescription());
        Difficulty difficulty = difficultyRepository.findById(taskRequest.getDifficultId()).orElse(null);
        Project project = projectRepository.findById(taskRequest.getIdProject()).orElse(null);
        if (difficulty == null || project == null) {
            throw new CustormException(Message.DATA_NOT_EXIST);
        }
        task.setDifficulty(difficulty);
        task.setProject(project);
        task.setTitle(taskRequest.getTitle());
        task.setPoint(0f);
        task.setDone(false);
        Date date = DatetimeUtils.convertStringToDateOrNull(taskRequest.getDueDate(), DatetimeUtils.YYYYMMDD);
        if (DatetimeUtils.getDayBetweenTwoDiffDate(date, new Date()) <= 1) {
            throw new CustormException(Message.INVALID_DATE);
        }
        task.setDueDate(DatetimeUtils.convertStringToDateOrNull(taskRequest.getDueDate(), DatetimeUtils.YYYYMMDD));
        taskRepository.save(task);
    }

    @Override
    public List<TaskDomain> getAllTask(int projectId) {
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