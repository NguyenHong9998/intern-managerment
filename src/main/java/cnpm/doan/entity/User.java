package cnpm.doan.entity;


import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Where(clause = "is_deleted = 0")
public class User {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_seq")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "gender")
    private String gender;
    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role roles;
    @ManyToOne()
    @JoinColumn(name = "department_id")
    private Department department;
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "int default 0")
    private int isDeleted = 0;

    public User() {
    }

    public User(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(long id, String name, String password, String email, String address, String gender, Role roles, Department department) {
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.roles = roles;
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

}