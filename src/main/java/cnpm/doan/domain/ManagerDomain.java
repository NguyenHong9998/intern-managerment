package cnpm.doan.domain;

import cnpm.doan.entity.User;

import java.util.List;

public class ManagerDomain extends UserDomain{
    List<PermissionDomain> permissionDomains;

    public ManagerDomain(List<PermissionDomain> permissionDomains) {
        this.permissionDomains = permissionDomains;
    }

    public ManagerDomain(int id, String name, String email, String department, String address, String gender, String role, List<PermissionDomain> permissionDomains) {
        super(id, name, email, department, address, gender, role);
        this.permissionDomains = permissionDomains;
    }

    public ManagerDomain(User user, List<PermissionDomain> permissionDomains) {
        super(user);
        this.permissionDomains = permissionDomains;
    }

    public List<PermissionDomain> getPermissionDomains() {
        return permissionDomains;
    }

    public void setPermissionDomains(List<PermissionDomain> permissionDomains) {
        this.permissionDomains = permissionDomains;
    }
}
