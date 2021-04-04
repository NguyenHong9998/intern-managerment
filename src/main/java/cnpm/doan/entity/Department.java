package cnpm.doan.entity;

import javax.persistence.*;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @SequenceGenerator(name = "department_seq", sequenceName = "department_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "department_seq")
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    public Department() {

    }

    public Department(int id, String name) {
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
