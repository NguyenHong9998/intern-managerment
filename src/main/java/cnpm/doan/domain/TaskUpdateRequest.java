package cnpm.doan.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TaskUpdateRequest {
    private long taskId;
    private String description;
    private String title;
    private int difficulty;
    private boolean isDone;
    private String point;
    private String assignee;
    private List<UserContributeToTask> userTaskDomains;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty (int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public List<UserContributeToTask> getUserTaskDomains() {
        return userTaskDomains;
    }

    public void setUserTaskDomains(List<UserContributeToTask> userTaskDomains) {
        this.userTaskDomains = userTaskDomains;
    }
}
