package cnpm.doan.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "member_task")
public class MemberTask {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "membertask_seq", sequenceName = "membertask_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "membertask_seq")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete( action = OnDeleteAction.CASCADE )
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @OnDelete( action = OnDeleteAction.CASCADE )
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
