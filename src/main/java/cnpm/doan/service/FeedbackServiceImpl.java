package cnpm.doan.service;

import cnpm.doan.domain.FeedbackDomain;
import cnpm.doan.domain.UpdateFeedbackDomain;
import cnpm.doan.entity.Feedback;
import cnpm.doan.entity.Task;
import cnpm.doan.entity.User;
import cnpm.doan.repository.FeedbackRepository;
import cnpm.doan.repository.TaskRepository;
import cnpm.doan.repository.UserRepository;
import cnpm.doan.security.JwtUtil;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

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
        feedbackRepository.save(feedback);
    }
}
