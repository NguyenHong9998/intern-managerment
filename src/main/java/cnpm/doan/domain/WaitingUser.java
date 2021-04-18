package cnpm.doan.domain;

import cnpm.doan.entity.User;

public class WaitingUser {
    private String name;
    private String email;
    private int id;

    public WaitingUser(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public WaitingUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.id = user.getId();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
