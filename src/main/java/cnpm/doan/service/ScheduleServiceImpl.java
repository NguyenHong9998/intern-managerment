package cnpm.doan.service;

import cnpm.doan.domain.LeaveDomain;
import cnpm.doan.domain.ScheduleDomain;
import cnpm.doan.entity.Schedule;
import cnpm.doan.entity.User;
import cnpm.doan.repository.ScheduleRepository;
import cnpm.doan.repository.UserRepository;
import cnpm.doan.security.JwtUtil;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.DatetimeUtils;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void add(LeaveDomain leaveDomain) throws CustormException {
        Date date = DatetimeUtils.convertStringToDateOrNull(leaveDomain.getLeaveDate(), DatetimeUtils.YYYYMMDD);
        Schedule scheduleOld = scheduleRepository.findByUserIdAndTime(jwtUtil.getCurrentUser().getUserId(), date);
        if (scheduleOld != null && leaveDomain.getShift() == scheduleOld.getShift()) {
            throw new CustormException(Message.CANNOT_ADD_LEAVE);
        }
        User user = userRepository.findById(jwtUtil.getCurrentUser().getUserId()).orElse(null);
        Schedule schedule = new Schedule();
        schedule.setReasonLeave(leaveDomain.getReasonContent());
        schedule.setTime(date);
        schedule.setShift(leaveDomain.getShift());
        schedule.setUser(user);
        scheduleRepository.save(schedule);
    }

    @Override
    public void update(int leaveId, LeaveDomain leaveDomain) throws CustormException {
        Schedule schedule = scheduleRepository.findById(leaveId).orElse(null);
        if (schedule == null) {
            throw new CustormException(Message.INVALID_LEAVE_ID);
        }
        Date date = DatetimeUtils.convertStringToDateOrNull(leaveDomain.getLeaveDate(), DatetimeUtils.YYYYMMDD);
        Schedule scheduleOld = scheduleRepository.findByUserIdAndTime(jwtUtil.getCurrentUser().getUserId(), date);
        if (scheduleOld != null && leaveDomain.getShift() == schedule.getShift()) {
            throw new CustormException(Message.CANNOT_ADD_LEAVE);
        }
        scheduleOld.setReasonLeave(leaveDomain.getReasonContent());
        scheduleOld.setTime(date);
        scheduleOld.setShift(leaveDomain.getShift());
        scheduleRepository.save(scheduleOld);
    }

    @Override
    public void delete(int leaveId) throws CustormException {
        Schedule schedule = scheduleRepository.findById(leaveId).orElse(null);
        User user = userRepository.findById(jwtUtil.getCurrentUser().getUserId()).orElse(null);
        if (schedule.getUser().getId() != jwtUtil.getCurrentUser().getUserId() && user.getRoles().getRoleName().equals("ROLE_USER")) {
            throw new CustormException(Message.CANNOT_DELETE_ANOTHER_SCHEDULE);
        }
        scheduleRepository.delete(schedule);
    }

    @Override
    public List<ScheduleDomain> getAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        List<ScheduleDomain> scheduleDomains = schedules.stream().map(t -> new ScheduleDomain(t)).collect(Collectors.toList());
        return scheduleDomains;
    }
}
