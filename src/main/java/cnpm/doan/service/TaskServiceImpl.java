package cnpm.doan.service;

import cnpm.doan.domain.TaskDomain;
import cnpm.doan.domain.TaskRequest;
import cnpm.doan.domain.TaskUpdateRequest;
import cnpm.doan.domain.UserContributeToTask;
import cnpm.doan.entity.*;
import cnpm.doan.repository.*;
import cnpm.doan.security.JwtUtil;
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

    @Autowired
    MemberProjectRepository memberProjectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    FeedbackRepository feedbackRepository;

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
    public List<TaskDomain> getAllTask(int projectId) throws CustormException {
        User user = userRepository.findById(jwtUtil.getCurrentUser().getUserId()).orElse(null);
        System.out.println("xxxxxxxxxxxxxxxxxxxx:" + user);
        if (!user.getRoles().getRoleName().equals("ROLE_ADMIN")) {
            MemberProject memberProject = memberProjectRepository.findMemberProjectByUserIdAndProjectId(user.getId(), projectId);
            if (memberProject == null) {
                throw new CustormException(Message.INVALID_USER_PROJECT);
            }
        }
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
            List<UserContributeToTask> taskDomains =
                    memberTasks.stream().map(utd -> {
                        UserContributeToTask userContributeToTask = new UserContributeToTask();
                        userContributeToTask.setId(utd.getUser().getId());
                        userContributeToTask.setName(utd.getUser().getName());
                        return userContributeToTask;
                    }).collect(Collectors.toList());
            taskDomain.setUserTaskDomains(taskDomains);
            result.add(taskDomain);
        }
        return result;
    }

    @Override
    public void deleteTask(int taskId) throws CustormException {
        feedbackRepository.deleteByTaskId(taskId);
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new CustormException(Message.INVALID_TASK);
        }
        if (!task.isDone()) {
            throw new CustormException(Message.TASK_NOT_DONE);
        }
//        List<MemberTask> memberTasks = memberTaskRepository.findAllByTaskId(taskId);
//        memberTasks.forEach(t -> memberTaskRepository.deleteByTaskId(t.getTask().getId()));
        memberTaskRepository.deleteByTaskId(task.getId());
        taskRepository.deleteById(taskId);
    }

    @Override
    public void update(TaskUpdateRequest taskUpdateRequest) throws CustormException {
        Task task = taskRepository.findById(taskUpdateRequest.getTaskId()).orElse(null);
        if (task == null) {
            throw new CustormException(Message.INVALID_TASK);
        }
        task.setDueDate(DatetimeUtils.convertStringToDateOrNull(taskUpdateRequest.getDuedate(), DatetimeUtils.YYYYMMDD));
        task.setDescription(taskUpdateRequest.getDescription());
        task.setTitle(taskUpdateRequest.getTitle());
        task.setDone(taskUpdateRequest.isDone());
        task.setPoint(Float.valueOf(taskUpdateRequest.getPoint()));
        taskRepository.save(task);
        memberTaskRepository.deleteByTaskId(task.getId());

        for (UserContributeToTask user : taskUpdateRequest.getUserTaskDomains()) {

            User userInTask = userRepository.findById(user.getId()).orElse(null);
            MemberTask memberTask = new MemberTask();
            memberTask.setTask(task);
            memberTask.setUser(userInTask);
            memberTaskRepository.save(memberTask);
        }
    }
}
