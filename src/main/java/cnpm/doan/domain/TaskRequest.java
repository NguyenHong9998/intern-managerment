package cnpm.doan.domain;

public class TaskRequest {
    private String description;
    private String title;
    private int difficultId;
    private int idProject;
    private String dueDate;

    public TaskRequest() {

    }

    public TaskRequest(String description, String title, int difficultId, int idProject) {
        this.description = description;
        this.title = title;
        this.difficultId = difficultId;
        this.idProject = idProject;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

    public int getDifficultId() {
        return difficultId;
    }

    public void setDifficultId(int difficultId) {
        this.difficultId = difficultId;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }
}
