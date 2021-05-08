package cnpm.doan.repository;

import cnpm.doan.entity.MemberProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MemberProjectRepository extends JpaRepository<MemberProject, Integer> {
    MemberProject findMemberProjectByUserIdAndProjectId(int userId, int projectId);

    List<MemberProject> findMemberProjectByProjectId(int idProject);
}
