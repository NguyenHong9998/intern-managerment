package cnpm.doan.domain;

public class GetAllProjectDomain {
    private int  projectId;
    private String description;
    private String dueDate;
    private String title;
    private String managerName;
    private String startDate;

    public GetAllProjectDomain() {

    }

    public GetAllProjectDomain(int projectId, String description, String dueDate, String startDate, String title, String managerName) {
        this.description = description;
        this.dueDate = dueDate;
        this.startDate=startDate;
        this.title = title;
        this.managerName = managerName;
        this.projectId = projectId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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

    @Override
    public String toString() {
        return "GetAllProjectDomain{" +
                "projectId='" + projectId + '\'' +
                ", description='" + description + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", title='" + title + '\'' +
                ", managerName='" + managerName + '\'' +
                '}';
    }
}
