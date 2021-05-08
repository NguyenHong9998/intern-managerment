package cnpm.doan.service;

import cnpm.doan.entity.Role;

import java.util.List;

public interface RoleService {
    Role findRoleByRoleName(String roleName);
    List<Role> findAll();
}
