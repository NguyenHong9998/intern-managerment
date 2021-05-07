package cnpm.doan.service;

import cnpm.doan.domain.TaskDomain;
import cnpm.doan.domain.TaskRequest;
import cnpm.doan.domain.UserTaskDomain;
import cnpm.doan.entity.Difficulty;
import cnpm.doan.entity.MemberTask;
import cnpm.doan.entity.Project;
import cnpm.doan.entity.Task;
import cnpm.doan.repository.DifficultyRepository;
import cnpm.doan.repository.MemberTaskRepository;
import cnpm.doan.repository.ProjectRepository;
import cnpm.doan.repository.TaskRepository;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.DatetimeUtils;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    MemberTaskRepository memberTaskRepository;

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
        if (date == null) {
            throw new CustormException(Message.INVALID_DATE);
        }
        if (DatetimeUtils.getDayBetweenTwoDiffDate(date, new Date()) <= 1) {
            throw new CustormException(Message.INVALID_DATE);
        }
        task.setDueDate(DatetimeUtils.convertStringToDateOrNull(taskRequest.getDueDate(), DatetimeUtils.YYYYMMDD));
        task.setCreateDate(new Date());
        taskRepository.save(task);
    }

    @Override
    public List<TaskDomain> getAllTask(int projectId) {
        List<Task> findAllTask = findAllTaskByProjectId(projectId);
        List<TaskDomain> result = new ArrayList<>();
        for (Task task : findAllTask) {

            TaskDomain taskDomain = new TaskDomain();
            taskDomain.setTaskId(task.getId());
            taskDomain.setDescription(task.getDescription());
            taskDomain.setTitle(task.getTitle());
            taskDomain.setDifficulty(task.getDifficulty().getName());
            taskDomain.setPoint(String.format("%.2s", task.getPoint()));
            taskDomain.setProjectName(task.getProject().getTitle());
            taskDomain.setIsDone(task.isDone());
            taskDomain.setCreateDate(task.getCreateDate().toString());
            List<MemberTask> memberTasks = memberTaskRepository.findAllByTaskId(task.getId());
            List<UserTaskDomain> taskDomains =
                    memberTasks.stream().map(utd -> {
                        return new UserTaskDomain(utd.getUser().getId(), utd.getUser().getName());
                    }).collect(Collectors.toList());
            taskDomain.setUserTaskDomains(taskDomains);
            result.add(taskDomain);
        }
        return result;
    }

    @Override
    public void deleteTask(int taskId) throws CustormException {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new CustormException(Message.INVALID_TASK);
        }
        if (!task.isDone()) {
            throw new CustormException(Message.TASK_NOT_DONE);
        }
        List<MemberTask> memberTasks = memberTaskRepository.findAllByTaskId(taskId);
        memberTasks.forEach(t -> memberTaskRepository.deleteByTaskId(t.getTask().getId()));
        taskRepository.deleteById(taskId);
    }
}
