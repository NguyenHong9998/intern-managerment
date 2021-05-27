package cnpm.doan.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "feedback_seq", sequenceName = "feedback_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "feedback_seq")
    private int id;
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Column(name = "shift")
    private int shift;
    @Column(name = "reason_leave")
    private String reasonLeave;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public String getReasonLeave() {
        return reasonLeave;
    }

    public void setReasonLeave(String reasonLeave) {
        this.reasonLeave = reasonLeave;
    }
}
