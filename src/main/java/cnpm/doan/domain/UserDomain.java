package cnpm.doan.domain;

import cnpm.doan.entity.User;

public class UserDomain {
    private long id;
    private String name;
    private String email;
    private String department;
    private String address;
    private String gender;
    private String role;

    public UserDomain(String name, String email, String department, String address, String gender, String role) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.address = address;
        this.gender = gender;
        this.role = role;
    }

    public UserDomain(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.department = user.getDepartment().getName();
        this.address = user.getAddress();
        this.role = user.getRoles().getRoleName();
        this.gender = user.getGender();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

}
