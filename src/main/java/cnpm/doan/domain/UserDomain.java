package cnpm.doan.domain;

import cnpm.doan.entity.User;

public class UserDomain {
    private int id;
    private String name;
    private String email;
    private String department;
    private String address;
    private String gender;
    private String role;

    public UserDomain() {

    }

    public UserDomain(int id, String name, String email, String department, String address, String gender, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.address = address;
        this.gender = gender;
        this.role = role;
    }

    public UserDomain(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        if (user.getDepartment() != null) {
            this.department = user.getDepartment().getName();
        }
        this.address = user.getAddress();
        if (user.getRoles() != null) {
            this.role = user.getRoles().getRoleName();
        }
        this.gender = user.getGender();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserDomain{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
