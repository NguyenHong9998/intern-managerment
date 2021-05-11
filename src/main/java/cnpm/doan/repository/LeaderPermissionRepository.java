package cnpm.doan.repository;

import cnpm.doan.entity.LeadPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderPermissionRepository extends JpaRepository<LeadPermission, Integer> {
    List<LeadPermission> findAllByUserId(int userId);
}
