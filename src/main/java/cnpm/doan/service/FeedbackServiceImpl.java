package cnpm.doan.service;

import cnpm.doan.domain.FeedbackDomain;
import cnpm.doan.domain.UpdateFeedbackDomain;
import cnpm.doan.domain.UserContributeToTask;
import cnpm.doan.entity.Feedback;
import cnpm.doan.entity.MemberTask;
import cnpm.doan.entity.Task;
import cnpm.doan.entity.User;
import cnpm.doan.repository.FeedbackRepository;
import cnpm.doan.repository.MemberTaskRepository;
import cnpm.doan.repository.TaskRepository;
import cnpm.doan.repository.UserRepository;
import cnpm.doan.security.JwtUtil;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberTaskRepository memberTaskRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;


    @Override
    public List<Feedback> getAllFeedbackByTaskId(int taskId) {
        List<Feedback> feedbacks = feedbackRepository.findFeedbackByTaskId(taskId);
        return feedbacks;
    }

    @Override
    public void addFeedback(FeedbackDomain feedbackDomain) throws CustormException {
        Task task = taskRepository.findById(feedbackDomain.getTaskId()).orElse(null);
        if (task == null) {
            throw new CustormException(Message.INVALID_TASK);
        }
        Feedback feedback = new Feedback();
        feedback.setTime(new Date());
        feedback.setTask(task);
        feedback.setMessage(feedbackDomain.getFeedbackContent());
        User user = userRepository.findById(jwtUtil.getCurrentUser().getUserId()).orElse(null);
        feedback.setUser(user);
        feedbackRepository.save(feedback);
        List<MemberTask> memberTasks = memberTaskRepository.findAllByTaskId(task.getId());
        List<User> users = memberTasks.stream().map(t->t.getUser()).collect(Collectors.toList());
        for(User user1 : users){
            String email = user1.getEmail();
            try {
                sendEmail(email, user.getName(), task.getId() + ": "+ task.getTitle());
            } catch (Exception e) {
                System.out.println("Error when send feedback email");
            }
        }
    }

    @Transactional
    @Override
    public void delete(int feedbackId) throws CustormException {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null) {
            throw new CustormException(Message.INVALID_FEEDBACK);
        }
        User user = userRepository.findById(jwtUtil.getCurrentUser().getUserId()).orElse(null);
        if (user.getId() != feedback.getUser().getId() && user.getRoles().getRoleName().equals("ROLE_USER")) {
            throw new CustormException(Message.CANNOT_DELETE_ANOTHER_FEED);
        }
        feedbackRepository.deleteById(feedbackId);
    }

    @Override
    public void update(UpdateFeedbackDomain feedbackDomain) throws CustormException {
        Feedback feedback = feedbackRepository.findById(feedbackDomain.getFeedbackId()).orElse(null);
        if (feedback == null) {
            throw new CustormException(Message.INVALID_FEEDBACK);
        }
        if (jwtUtil.getCurrentUser().getUserId() != feedback.getUser().getId()) {
            throw new CustormException(Message.CANNOT_UPDATE_ANOTHER_FEED);
        }
        feedback.setMessage(feedbackDomain.getFeedbackContent());
        feedback.setTime(new Date());
        feedbackRepository.save(feedback);
    }
    public void sendEmail(String recipientEmail, String userComment, String taskName)
            throws javax.mail.MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(recipientEmail);
        helper.setSubject(taskName);
        String content = "<img src='https://res-1.cloudinary.com/crunchbase-production/image/upload/c_lpad,f_auto,q_auto:eco/yrunsh3clxeoqgfblejv'> "
                + "<br>"
                + "<br>"
                + "<p>"+ userComment + " add new comment to this task"+"</p>"
                + "<p>You are receiving this email because you contribute on the task</p>"
                + "<br>"
                + "Have a nice day.</p>";
        ;
        helper.setText(content, true);
        mailSender.send(message);
    }
}
