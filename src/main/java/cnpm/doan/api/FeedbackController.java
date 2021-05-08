package cnpm.doan.api;

import cnpm.doan.domain.FeedbackDomain;
import cnpm.doan.domain.FeedbackResponseDomain;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.UpdateFeedbackDomain;
import cnpm.doan.entity.Feedback;
import cnpm.doan.service.FeedbackService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/task/feedback")
    public ResponseEntity<?> getFeedbackByTaskId(@RequestParam("task_id") int taskId) {
        List<Feedback> feedbacks = feedbackService.getAllFeedbackByTaskId(taskId);
        List<FeedbackResponseDomain> result = feedbacks.stream().map(t -> {
            FeedbackResponseDomain domain = new FeedbackResponseDomain();
            domain.setFeedbackContent(t.getMessage());
            domain.setTaskId(t.getId());
            return domain;
        }).collect(Collectors.toList());
        if (feedbacks.size() == 0) {
            return ResponseEntity.ok(new ResponeDomain(Message.EMPTY_RESULT.getDetail(), HTTPStatus.success));
        }
        return ResponseEntity.ok(new ResponeDomain(feedbacks, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/task/feedback/add")
    public ResponseEntity<?> addFeedbackByTaskId(@RequestBody FeedbackDomain feedbackDomain) {
        try {
            feedbackService.addFeedback(feedbackDomain);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(feedbackDomain, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/task/feedback/update")
    public ResponseEntity<?> updateFeedback(@RequestBody UpdateFeedbackDomain feedbackDomain) {
        try {
            feedbackService.update(feedbackDomain);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(feedbackDomain, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN','ROLE_MANAGER')")
    @PutMapping("/task/feedback/delete")
    public ResponseEntity<?> deleteFeedback(@RequestParam("feedback_id") int feedbackId) {
        try {
            feedbackService.delete(feedbackId);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), false));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
}
