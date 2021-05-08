package cnpm.doan.service;

import cnpm.doan.domain.LeaveDomain;

public interface ScheduleService {
    void add(LeaveDomain leaveDomain);

    void update(LeaveDomain leaveDomain);

    void delete(int leaveId);
}
