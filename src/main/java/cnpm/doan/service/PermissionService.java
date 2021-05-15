package cnpm.doan.service;

import cnpm.doan.domain.PermissionDomain;
import cnpm.doan.domain.PermissionManagerDomain;
import cnpm.doan.entity.PermissionEntity;
import cnpm.doan.util.CustormException;

import java.util.List;

public interface PermissionService {
    List<PermissionEntity> getAllPermission();
    List<PermissionDomain> getPermissionOfManager(int managerId) throws CustormException;

    void addPermissionOfManager(PermissionManagerDomain permissionManagerDomain) throws CustormException;
}
