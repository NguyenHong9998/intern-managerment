package cnpm.doan.domain;

public class ManagerInforDomain {
    private long managerId;
    private String name;
    private String username;

    public ManagerInforDomain(long managerId, String name, String username) {
        this.managerId = managerId;
        this.name = name;
        this.username = username;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
