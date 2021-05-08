package cnpm.doan.domain;

import cnpm.doan.entity.Schedule;
import cnpm.doan.util.DatetimeUtils;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ScheduleDomain {
    private int id;
    private String reason;
    private String time;
    private int shift;
    private String userName;

    public ScheduleDomain() {

    }

    public ScheduleDomain(int id, String reason, String time, int shift, String userName) {
        this.id = id;
        this.reason = reason;
        this.time = time;
        this.shift = shift;
        this.userName = userName;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public ScheduleDomain(Schedule schedule) {
        this.id = schedule.getId();
        this.reason = schedule.getReasonLeave();
        this.time = DatetimeUtils.convertDateToString(schedule.getTime());
        this.shift = schedule.getShift();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
