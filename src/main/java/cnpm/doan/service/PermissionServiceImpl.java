package cnpm.doan.service;

import cnpm.doan.domain.PermissionDomain;
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
    public List<PermissionDomain> getPermissionOfManager(int managerId) throws CustormException {
        User manager = userRepository.findById(managerId).orElse(null);
        if (manager == null || !manager.getRoles().getRoleName().equals("ROLE_MANAGER")) {
            throw new CustormException(Message.INVALID_MANGER);
        }
        List<PermissionDomain> permissionEntities = leaderPermissionRepository.findAllByUserId(managerId)
                .stream().map(t -> {
                    PermissionDomain permissionDomain = new PermissionDomain();
                    permissionDomain.setId(t.getPermission().getId());
                    permissionDomain.setName(t.getPermission().getName());
                    return permissionDomain;
                }).collect(Collectors.toList());
        return permissionEntities;
    }

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
    public void editPermissonOfManager(PermissionManagerDomain permissionManagerDomain) throws CustormException {
        User manager = userRepository.findById(permissionManagerDomain.getManagerId()).orElse(null);
        if (manager == null || !manager.getRoles().getRoleName().equals("ROLE_MANAGER")) {
            throw new CustormException(Message.INVALID_MANGER);
        }
        List<LeadPermission> permissionEntities = leaderPermissionRepository.findAllByUserId(permissionManagerDomain.getManagerId()).stream().map(t -> {
            t.setIsDeleted(1);
            return t;
        }).collect(Collectors.toList());
        leaderPermissionRepository.saveAll(permissionEntities);
        List<PermissionEntity> savePermission = permissionRepository.findAllById(permissionManagerDomain.getPermissionId());
        List<LeadPermission> leadPermissions = savePermission.stream().map(t -> {
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
