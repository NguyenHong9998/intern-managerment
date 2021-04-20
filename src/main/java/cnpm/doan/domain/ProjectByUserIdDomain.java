package cnpm.doan.domain;

public class ProjectByUserIdDomain extends GetAllProjectDomain {
    private String userId;

    public ProjectByUserIdDomain() {

    }

    public ProjectByUserIdDomain(String projectId, String description, String dueDate, String title, String managerName, String userId) {
        super(projectId, description, dueDate, title, managerName);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
