package cnpm.doan.service;

import cnpm.doan.domain.PermissionManagerDomain;
import cnpm.doan.entity.PermissionEntity;
import cnpm.doan.util.CustormException;

import java.util.List;

public interface PermissionService {
    List<PermissionEntity> getAllPermission();
    List<PermissionEntity> getPermissionOfManager(int managerId) throws CustormException;

    void addPermissionOfManager(PermissionManagerDomain permissionManagerDomain) throws CustormException;
}
