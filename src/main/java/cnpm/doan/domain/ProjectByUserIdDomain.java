package cnpm.doan.domain;

public class ProjectByUserIdDomain {
    private String userId;
    private String description;
    private String dueDate;
    private String title;
    private String managerName;

    public ProjectByUserIdDomain() {

    }

    public ProjectByUserIdDomain(String userId, String description, String dueDate, String title, String managerName) {
        this.userId = userId;
        this.description = description;
        this.dueDate = dueDate;
        this.title = title;
        this.managerName = managerName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
