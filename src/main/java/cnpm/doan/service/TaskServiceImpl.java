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

import javax.transaction.Transactional;
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

    @Autowired
    MemberTaskService memberTaskService;

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
        task.setDueDate(DatetimeUtils.convertStringToDateOrNull(taskRequest.getDueDate(), DatetimeUtils.YYYYMMDD));
        task.setCreateDate(new Date());
        taskRepository.save(task);
    }

    @Override
    public List<TaskDomain> getAllTask(int projectId) throws CustormException {
        User user = userRepository.findById(jwtUtil.getCurrentUser().getUserId()).orElse(null);
//        if (!user.getRoles().getRoleName().equals("ROLE_ADMIN")) {
//            MemberProject memberProject = memberProjectRepository.findMemberProjectByUserIdAndProjectId(user.getId(), projectId);
//            Project project = projectRepository.findById(projectId).orElse(null);
//            if (project.getManager().getId() != user.getId() || memberProject == null) {
//                throw new CustormException(Message.INVALID_USER_PROJECT);
//            }
//        }
        List<Task> findAllTask = findAllTaskByProjectId(projectId);
        List<TaskDomain> result = new ArrayList<>();
        for (Task task : findAllTask) {
            TaskDomain taskDomain = new TaskDomain();
            taskDomain.setTaskId(task.getId());
            taskDomain.setDescription(task.getDescription());
            taskDomain.setTitle(task.getTitle());
            taskDomain.setDifficulty(task.getDifficulty().getName());
            taskDomain.setPoint(String.valueOf(task.getPoint()));
            taskDomain.setProjectName(task.getProject().getTitle());
            taskDomain.setIsDone(task.isDone());
            taskDomain.setCreateDate(task.getCreateDate().toString());
            if (task.getDueDate() != null) {
                taskDomain.setDueDate(task.getDueDate().toString());
            }
            List<MemberTask> memberTasks = memberTaskRepository.findAllByTaskId(task.getId());
            List<UserContributeToTask> taskDomains =
                    memberTasks.stream().map(utd -> {
                        UserContributeToTask userContributeToTask = new UserContributeToTask();
                        if (utd.getUser() != null) {
                            userContributeToTask.setId(utd.getUser().getId());
                            userContributeToTask.setName(utd.getUser().getName());
                            return userContributeToTask;
                        }
                        return null;
                    }).collect(Collectors.toList());
            taskDomain.setUsersAssignee(taskDomains.stream().filter(t -> t != null).collect(Collectors.toList()));
            result.add(taskDomain);
        }
        return result;
    }

    @Transactional
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

    @Transactional
    @Override
    public void update(int taskId, TaskUpdateRequest taskUpdateRequest) throws CustormException {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new CustormException(Message.INVALID_TASK);
        }
        task.setDueDate(DatetimeUtils.convertStringToDateOrNull(taskUpdateRequest.getDueDate(), DatetimeUtils.YYYYMMDD));
        task.setDescription(taskUpdateRequest.getDescription());
        task.setTitle(taskUpdateRequest.getTitle());
        task.setDone(taskUpdateRequest.isDone());
        task.setPoint(Float.parseFloat(taskUpdateRequest.getPoint()));
        task.setDifficulty(difficultyRepository.findById(Integer.valueOf(taskUpdateRequest.getDifficulty())).orElse(null));
        taskRepository.save(task);
        memberTaskRepository.deleteByTaskId(task.getId());

        for (UserContributeToTask user : taskUpdateRequest.getUsersAssignee()) {
            User userInTask = userRepository.findById(user.getId()).orElse(null);
            if (userInTask == null) {
                throw new CustormException(Message.INVALID_USER);
            }
            MemberTask memberTask = new MemberTask();
            memberTask.setTask(task);
            memberTask.setUser(userInTask);
            memberTaskRepository.save(memberTask);
        }
    }
}
