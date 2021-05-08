package cnpm.doan.api;

import cnpm.doan.domain.LeaveDomain;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.domain.ScheduleDomain;
import cnpm.doan.service.ScheduleService;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/schedule/add")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<?> addLeaveRequest(@RequestBody LeaveDomain leaveDomain) {
        try {
            scheduleService.add(leaveDomain);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), HTTPStatus.fail));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @GetMapping("/schedules")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<?> getAllSchedule() {
        List<ScheduleDomain> result = scheduleService.getAll();
        if(result.size() ==0){
            return ResponseEntity.ok(new ResponeDomain(result, Message.EMPTY_RESULT.getDetail(), HTTPStatus.success));
        }
        return ResponseEntity.ok(new ResponeDomain(result, Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }

    @PutMapping("/schedule/update")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<?> upadateSchedule(@RequestParam("leave_id") int leaveId, @RequestBody LeaveDomain leaveDomain) {
        try {
            scheduleService.update(leaveId, leaveDomain);
        } catch (CustormException e) {
            return ResponseEntity.ok(new ResponeDomain(e.getErrorType().getDetail(), HTTPStatus.fail));
        }
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
}
