package cnpm.doan.entity;


import javax.persistence.*;

@Entity
@Table(name = "permission")
public class PermissionEntity {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "permission_seq", sequenceName = "permission_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "permission_seq")
    private int id;
    @Column(name = "name")
    private String name;

    public PermissionEntity() {

    }

    public PermissionEntity(String name) {
        this.name = name;
    }

    public PermissionEntity(int id, String name) {
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

    @Override
    public String toString() {
        return "PermissionEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
