package cnpm.doan.service;

import cnpm.doan.entity.MemberProject;
import cnpm.doan.repository.MemberProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberProjectServiceImpl implements MemberProjectService {
    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Override
    public MemberProject findMemberProjectByUserIdAndProjectId(int userId, int projectId) {
        return memberProjectRepository.findMemberProjectByUserIdAndProjectId(userId, projectId);
    }

    @Override
    public void truncateTable() {
        memberProjectRepository.deleteAll();
    }
}
