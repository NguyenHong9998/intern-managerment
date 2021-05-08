package cnpm.doan.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "feedback_seq", sequenceName = "feedback_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "feedback_seq")
    private int id;
    @Column(name = "message")
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public Feedback() {

    }

    public Feedback(int id, String message, Date time, User user, Task task) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.user = user;
        this.task = task;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
