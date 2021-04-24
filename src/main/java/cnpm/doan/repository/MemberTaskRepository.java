package cnpm.doan.repository;

import cnpm.doan.entity.MemberTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTaskRepository extends JpaRepository<MemberTask, Integer> {
    void deleteByTaskId(int taskId);

    List<MemberTask> findAllByTaskId(int taskId);
}
