package cnpm.doan.service;

import cnpm.doan.domain.FeedbackDomain;
import cnpm.doan.domain.UpdateFeedbackDomain;
import cnpm.doan.entity.Feedback;
import cnpm.doan.util.CustormException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface FeedbackService {
    List<Feedback> getAllFeedbackByTaskId(int taskId);

    void addFeedback(FeedbackDomain feedbackDomain) throws CustormException;

    void delete(int feedbackId) throws CustormException;

    void update(UpdateFeedbackDomain feedbackDomain) throws CustormException;
}
