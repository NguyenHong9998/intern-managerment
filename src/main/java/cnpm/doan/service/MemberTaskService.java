package cnpm.doan.service;

import cnpm.doan.entity.User;
import cnpm.doan.util.CustormException;

import java.util.List;

public interface MemberTaskService {
    void assignUserToTask(int taskId, List<User> users) throws CustormException;
}
