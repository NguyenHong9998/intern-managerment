package cnpm.doan.domain;

import java.util.List;

public class TaskDomain {
    private long taskId;
    private String description;
    private String title;
    private String difficulty;
    private boolean isDone;
    private String point;
    private String projectName;
    private String createDate;
    private List<UserContributeToTask> usersAssignee;

    public TaskDomain() {
    }

    public TaskDomain(long taskId, String description, String title, String difficulty, boolean isDone, String point, String projectName, String createDate, List<UserContributeToTask> userTaskDomains) {
        this.taskId = taskId;
        this.description = description;
        this.title = title;
        this.difficulty = difficulty;
        this.isDone = isDone;
        this.point = point;
        this.projectName = projectName;
        this.createDate = createDate;
        this.usersAssignee = userTaskDomains;
    }

    public List<UserContributeToTask> getUsersAssignee() {
        return usersAssignee;
    }

    public void setUsersAssignee(List<UserContributeToTask> usersAssignee) {
        this.usersAssignee = usersAssignee;
    }

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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

}


