package cnpm.doan.service;

import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.entity.MemberProject;
import cnpm.doan.entity.MemberTask;
import cnpm.doan.entity.Task;
import cnpm.doan.entity.User;
import cnpm.doan.repository.MemberProjectRepository;
import cnpm.doan.repository.MemberTaskRepository;
import cnpm.doan.repository.TaskRepository;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberTaskServiceImpl implements MemberTaskService {
    @Autowired
    MemberProjectRepository memberProjectRepository;

    @Autowired
    MemberTaskRepository memberTaskRepository;

    @Autowired
    TaskRepository taskRepository;

    @Override
    public void assignUserToTask(int taskId, List<User> users) throws CustormException {
        Task task = taskRepository.findById(taskId).orElse(null);
        for (User user : users) {
            if (user == null) {

            }
            MemberProject memberProject = memberProjectRepository.findMemberProjectByUserIdAndProjectId(user.getId(), task.getProject().getId());
            if (memberProject == null) {
                throw new CustormException(Message.INVALID_USER);
            }
            MemberTask memberTask = new MemberTask();
            memberTask.setTask(task);
            memberTask.setUser(user);
            memberTaskRepository.save(memberTask);
        }
    }
}
