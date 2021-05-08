package cnpm.doan.service;

import cnpm.doan.domain.LeaveDomain;
import cnpm.doan.domain.ScheduleDomain;
import cnpm.doan.entity.Schedule;
import cnpm.doan.util.CustormException;

import java.util.List;

public interface ScheduleService {
    void add(LeaveDomain leaveDomain) throws CustormException;

    void update(int leaveId,LeaveDomain leaveDomain) throws CustormException;

    void delete(int leaveId);

    List<ScheduleDomain> getAll();
}
