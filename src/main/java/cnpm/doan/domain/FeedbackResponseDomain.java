package cnpm.doan.domain;

import cnpm.doan.entity.Feedback;
import cnpm.doan.util.DatetimeUtils;

public class FeedbackResponseDomain {
    private int feedbackId;
    private String feedbackContent;
    private String date;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public FeedbackResponseDomain() {

    }

    public FeedbackResponseDomain(Feedback feedback) {
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

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
