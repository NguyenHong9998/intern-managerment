package cnpm.doan.domain;

import cnpm.doan.entity.Feedback;
import cnpm.doan.util.DatetimeUtils;

public class FeedbackResponseDomain {
    private int taskId;
    private String feedbackContent;
    private String date;
    private int feedbackId;

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public FeedbackResponseDomain() {

    }

    public FeedbackResponseDomain(Feedback feedback) {
        this.taskId = feedback.getTask().getId();
        this.date = DatetimeUtils.convertDateToString(feedback.getTime());
        this.feedbackContent = feedback.getMessage();
        this.feedbackId = feedback.getId();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
