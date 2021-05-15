package cnpm.doan.service;

import cnpm.doan.domain.PermissionManagerDomain;
import cnpm.doan.entity.LeadPermission;
import cnpm.doan.entity.PermissionEntity;
import cnpm.doan.entity.User;
import cnpm.doan.repository.LeaderPermissionRepository;
import cnpm.doan.repository.PermissionRepository;
import cnpm.doan.repository.UserRepository;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD4;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LeaderPermissionRepository leaderPermissionRepository;

    @Override
    public void addPermissionOfManager(PermissionManagerDomain permissionManagerDomain) throws CustormException {
        User manager = userRepository.findById(permissionManagerDomain.getManagerId()).orElse(null);
        if (manager == null || !manager.getRoles().getRoleName().equals("ROLE_MANAGER")) {
            throw new CustormException(Message.INVALID_MANGER);
        }
        List<Integer> permissionEntities = permissionManagerDomain.getPermissionId();

        List<PermissionEntity> permissions = permissionRepository.findAllById(permissionEntities);
        if (permissions.size() != permissionEntities.size()) {
            throw new CustormException(Message.INVALID_PERMISION_ID);
        }
        List<LeadPermission> leadPermissions = permissions.stream().map(t -> {
            LeadPermission leadPermission = new LeadPermission();
            leadPermission.setPermission(t);
            leadPermission.setUser(manager);
            return leadPermission;
        }).collect(Collectors.toList());

        leaderPermissionRepository.saveAll(leadPermissions);
    }

    @Override
    public List<PermissionEntity> getAllPermission() {
        return permissionRepository.findAll();
    }
}
