package cnpm.doan.repository;

import cnpm.doan.entity.MemberTask;
import cnpm.doan.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTaskRepository extends JpaRepository<MemberTask, Integer> {
    void deleteByTaskId(int taskId);

    List<MemberTask> findAllByTaskId(int taskId);

    List<MemberTask> findAllByUserId(int userId);

    @Query("select t from MemberTask mt inner join Task t on mt.task.id = t.id where mt.user.id = ?1")
    List<Task> findByUserId(int userId);
}
