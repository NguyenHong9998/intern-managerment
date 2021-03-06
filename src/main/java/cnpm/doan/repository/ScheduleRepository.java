package cnpm.doan.repository;

import cnpm.doan.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Schedule findByUserIdAndTime(int userId, Date date);

    List<Schedule> findAllByUserId(int userId);
}
