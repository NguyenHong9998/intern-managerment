package cnpm.doan.api;

import cnpm.doan.domain.LeaveDomain;
import cnpm.doan.domain.ResponeDomain;
import cnpm.doan.service.ScheduleService;
import cnpm.doan.util.HTTPStatus;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/schedule/add")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<?> addLeaveRequest(@RequestBody LeaveDomain leaveDomain) {
        scheduleService.add(leaveDomain);
        return ResponseEntity.ok(new ResponeDomain(Message.SUCCESSFUlLY.getDetail(), HTTPStatus.success));
    }
}
