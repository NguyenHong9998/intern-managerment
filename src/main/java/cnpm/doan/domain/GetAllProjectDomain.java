package cnpm.doan.domain;

import java.util.List;

public class GetAllProjectDomain {
    private int projectId;
    private String description;
    private String dueDate;
    private String title;
    private ManagerInforDomain managerName;
    private String startDate;
    List<UserProject> userAssignee;

    public GetAllProjectDomain() {

    }

    public GetAllProjectDomain(int projectId, String description, String dueDate, String startDate, String title, ManagerInforDomain managerName, List<UserProject> userAssignee) {
        this.description = description;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.title = title;
        this.managerName = managerName;
        this.projectId = projectId;
        this.userAssignee = userAssignee;
    }

    public List<UserProject> getUserAssignee() {
        return userAssignee;
    }

    public void setUserAssignee(List<UserProject> userAssignee) {
        this.userAssignee = userAssignee;
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

    public ManagerInforDomain getManagerName() {
        return managerName;
    }

    public void setManagerName(ManagerInforDomain managerName) {
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
