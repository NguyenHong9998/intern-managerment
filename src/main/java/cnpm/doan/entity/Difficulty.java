package cnpm.doan.entity;

import javax.persistence.*;

@Entity
@Table(name = "difficulty")
public class Difficulty {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "diff_seq", sequenceName = "diff_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "diff_seq")
    private int id;
    @Column(name = "name")
    private String name;

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
