package cnpm.doan.domain;

import cnpm.doan.entity.PermissionEntity;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PermissionManagerDomain {
    private int managerId;
    private List<Integer> permissionId;

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getManagerId() {
        return managerId;
    }

    public List<Integer> getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(List<Integer> permissionId) {
        this.permissionId = permissionId;
    }
}
