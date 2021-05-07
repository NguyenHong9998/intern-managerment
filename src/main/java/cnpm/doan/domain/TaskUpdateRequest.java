package cnpm.doan.domain;

import java.util.List;

public class TaskUpdateRequest {
    private long taskId;
    private String description;
    private String title;
    private String difficulty;
    private boolean isDone;
    private String point;
    private String assignee;
    private List<UserContributeToTask> userTaskDomains;
}
