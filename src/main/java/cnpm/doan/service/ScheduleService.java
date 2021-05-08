package cnpm.doan.service;

import cnpm.doan.domain.LeaveDomain;
import cnpm.doan.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    void add(LeaveDomain leaveDomain);

    void update(LeaveDomain leaveDomain);

    void delete(int leaveId);

    List<Schedule> getAll();
}
