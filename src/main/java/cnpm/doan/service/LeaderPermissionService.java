package cnpm.doan.service;

import cnpm.doan.entity.LeadPermission;

import java.util.List;

public interface LeaderPermissionService {
    List<LeadPermission> getLeadPermissionByLeadId(int leadId);
}
