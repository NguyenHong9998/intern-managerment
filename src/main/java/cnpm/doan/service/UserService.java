package cnpm.doan.service;


import cnpm.doan.domain.UserDomain;
import cnpm.doan.domain.WaitingUser;
import cnpm.doan.entity.User;
import cnpm.doan.security.UserPrincipal;
import cnpm.doan.util.CustormException;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User findUserByEmail(String email);

    UserPrincipal findByUsername(String email);

    List<User> findUserByRoleName(String roleName);

    User findUserByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);

    void updateResetPasswordToken(String token, String email) throws CustormException;

    List<WaitingUser> findWaittingUser();

    User findById(int id);

    void deleteUser(int idUser) throws CustormException;

    void denyUser(int[] userIds) throws CustormException;

    void acceptUsers(int[] userIds) throws CustormException;
}