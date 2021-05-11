package cnpm.doan.service;

import cnpm.doan.entity.Permission;
import cnpm.doan.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }
}
