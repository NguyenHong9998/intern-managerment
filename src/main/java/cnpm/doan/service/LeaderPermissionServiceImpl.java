package cnpm.doan.service;

import cnpm.doan.entity.LeadPermission;
import cnpm.doan.repository.LeaderPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderPermissionServiceImpl implements LeaderPermissionService {
    @Autowired
    LeaderPermissionRepository leaderPermissionRepository;

    @Override
    public List<LeadPermission> getLeadPermissionByLeadId(int leadId) {
        return leaderPermissionRepository.findAllByUserId(leadId);
    }
}
