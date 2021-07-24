package cnpm.doan.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)

public class UserResult {
    List<Float> point ;
    List<Integer> workingTime;

    public List<Float> getPoint() {
        return point;
    }

    public void setPoint(List<Float> point) {
        this.point = point;
    }

    public List<Integer> getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(List<Integer> workingTime) {
        this.workingTime = workingTime;
    }
}
