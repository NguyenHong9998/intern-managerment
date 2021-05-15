package cnpm.doan.repository;

import cnpm.doan.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByProjectId(int projectId);

    void deleteById(int id);

    @Modifying
    @Transactional
    @Query("update Task t set t.isDone = ?2 where t.id =?1")
    void updateStatus(int taskId, boolean status);
}
