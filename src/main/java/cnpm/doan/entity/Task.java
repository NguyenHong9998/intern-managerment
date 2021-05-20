package cnpm.doan.entity;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
@Where(clause = "is_deleted = 0")
public class Task {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "task_seq")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "is_done", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDone;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "due_date")
    private Date dueDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_id")
    private Difficulty difficulty;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "point", columnDefinition = "DECIMAL(7,2)")
    private float point;
    @Column(name = "is_deleted", nullable = false, columnDefinition = "int default 0")
    private int isDeleted = 0;

    public Project getProject() {
        return project;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
