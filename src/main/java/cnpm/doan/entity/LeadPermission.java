package cnpm.doan.entity;

import javax.persistence.*;

@Entity
@Table(name = "permission_user")
public class LeadPermission {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "permission_user_seq", sequenceName = "permission_user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "permission_user_seq")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private PermissionEntity permission;

    public LeadPermission() {
    }

    public LeadPermission(int id, User user, PermissionEntity permission) {
        this.id = id;
        this.user = user;
        this.permission = permission;
    }

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

    public PermissionEntity getPermission() {
        return permission;
    }

    public void setPermission(PermissionEntity permission) {
        this.permission = permission;
    }
}
