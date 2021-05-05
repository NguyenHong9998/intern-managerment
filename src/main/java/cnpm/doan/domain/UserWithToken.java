package cnpm.doan.domain;

import cnpm.doan.entity.User;

public class UserWithToken extends UserDomain {
    private String token;

    public UserWithToken(int id, String name, String email, String department, String address, String gender, String role, String token) {
        super(id, name, email, department, address, gender, role);
        this.token = token;
    }

    public UserWithToken(User user, String token) {
        super(user);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
