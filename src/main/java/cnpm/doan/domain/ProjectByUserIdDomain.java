package cnpm.doan.domain;

import java.util.List;

public class ProjectByUserIdDomain extends GetAllProjectDomain {
    private String userId;
    List<UserProject> userAssignee;


    public ProjectByUserIdDomain() {

    }

    public List<UserProject> getUserAssignee() {
        return userAssignee;
    }

    public void setUserAssignee(List<UserProject> userAssignee) {
        this.userAssignee = userAssignee;
    }

    public ProjectByUserIdDomain(String userId) {
        this.userId = userId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
