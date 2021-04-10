package cnpm.doan.domain;

public class ProjectDomain {
    private String title;
    private String description;
    private String dueDate;
    private int idOfAdmin;

    public ProjectDomain(String title, String description, String dueDate, int idOfAdmin) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.idOfAdmin = idOfAdmin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getIdOfAdmin() {
        return idOfAdmin;
    }

    public void setIdOfAdmin(int idOfAdmin) {
        this.idOfAdmin = idOfAdmin;
    }

    @Override
    public String toString() {
        return "ProjectDomain{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", usernameOfAdmin='" + idOfAdmin + '\'' +
                '}';
    }
}
