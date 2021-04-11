package cnpm.doan.service;

import cnpm.doan.entity.MemberProject;

public interface MemberProjectService {
    MemberProject findMemberProjectByUserIdAndProjectId(int userId, int projectId);
}
