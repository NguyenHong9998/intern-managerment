package cnpm.doan.repository;

import cnpm.doan.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findFeedbackByTaskId(int taskId);

    void deleteByTaskId(int taskId);

    void deleteById(int id);
}
