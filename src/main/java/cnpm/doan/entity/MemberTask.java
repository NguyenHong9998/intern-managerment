package cnpm.doan.entity;

import javax.persistence.*;

@Entity
@Table(name = "member_task")
public class MemberTask {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "membertask_seq", sequenceName = "membertask_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "membertask_seq")
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
