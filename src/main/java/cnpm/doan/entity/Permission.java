package cnpm.doan.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "permission_seq", sequenceName = "permission_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "permission_seq")
    private int id;
    @Column(name = "name")
    private String name;

    public Permission() {

    }

    public Permission(String name) {
        this.name = name;
    }

    public Permission(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
