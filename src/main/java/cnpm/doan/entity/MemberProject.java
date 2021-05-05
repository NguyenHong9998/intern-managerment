package cnpm.doan.entity;

import javax.persistence.*;

@Entity
@Table(name = "member_project")
public class MemberProject {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "memberpro_seq", sequenceName = "memberpro_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "memberpro_seq")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
