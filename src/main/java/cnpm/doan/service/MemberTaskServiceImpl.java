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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class MemberTaskServiceImpl implements MemberTaskService {
    @Autowired
    MemberProjectRepository memberProjectRepository;

    @Autowired
    MemberTaskRepository memberTaskRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void assignUserToTask(int taskId, List<User> users) throws CustormException {
        Task task = taskRepository.findById(taskId).orElse(null);
        for (User user : users) {
            MemberProject memberProject = memberProjectRepository.findMemberProjectByUserIdAndProjectId(user.getId(), task.getProject().getId());
            if (memberProject == null) {
                throw new CustormException(Message.INVALID_USER);
            }
            MemberTask memberTask = new MemberTask();
            memberTask.setTask(task);
            memberTask.setUser(user);
            memberTaskRepository.save(memberTask);
            try {
                sendEmail(user.getEmail(), task.getId() + ": "+task.getTitle());
            } catch (Exception e) {
                System.out.println("error when assign user to task");
            }
        }
    }

    public void sendEmail(String recipientEmail, String task)
            throws javax.mail.MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(recipientEmail);
        helper.setSubject(task);
        String content = "<img src='https://res-1.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco/yrunsh3clxeoqgfblejv'> "
                + "<br>"
                + "<br>"
                + "<p>You had assigned on this task</p>"
                + "<br>"
                + "Have a nice day.</p>";
        ;
        helper.setText(content, true);
        mailSender.send(message);
    }
}
