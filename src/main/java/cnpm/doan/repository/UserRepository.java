package cnpm.doan.repository;


import cnpm.doan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);

    User findUserByResetPasswordToken(String token);

    @Query("select new User(u.id, u.name, u.password, u.email,u.address, u.gender, u.roles, u.department) from User u where u.roles.roleName = ?1")
    List<User> findUserByRoleName(String roleName);

    @Query("select new User(u.id, u.name, u.password, u.email,u.address, u.gender, u.roles, u.department) from User u where u.roles")
    List<User> findWaitingUser();

}