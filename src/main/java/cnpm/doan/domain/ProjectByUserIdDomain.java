package cnpm.doan.domain;

public class ProjectByUserIdDomain extends GetAllProjectDomain {
    private String userId;

    public ProjectByUserIdDomain() {

    }

    public ProjectByUserIdDomain(String userId) {
        this.userId = userId;
    }

    public ProjectByUserIdDomain(String projectId, String description, String dueDate, String startDate, String title, String managerName, String userId) {
        super(projectId, description, dueDate, startDate, title, managerName);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
