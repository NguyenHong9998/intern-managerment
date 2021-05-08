package cnpm.doan.service;

import cnpm.doan.domain.LeaveDomain;
import cnpm.doan.entity.Schedule;
import cnpm.doan.util.DatetimeUtils;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Override
    public void add(LeaveDomain leaveDomain) {
        Schedule schedule = new Schedule();
        schedule.setReasonLeave(leaveDomain.getReasonContent());
        schedule.setTime(DatetimeUtils.convertStringToDateOrNull(leaveDomain.getLeaveDate(), DatetimeUtils.YYYYMMDD));
        schedule.setShift(leaveDomain.getShift());
    }

    @Override
    public void update(LeaveDomain leaveDomain) {

    }

    @Override
    public void delete(int leaveId) {

    }

}
