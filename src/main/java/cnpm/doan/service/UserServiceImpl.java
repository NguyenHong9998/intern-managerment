package cnpm.doan.service;

import cnpm.doan.domain.WaitingUser;
import cnpm.doan.entity.Role;
import cnpm.doan.entity.User;
import cnpm.doan.repository.RoleRepository;
import cnpm.doan.repository.UserRepository;
import cnpm.doan.security.UserPrincipal;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserPrincipal findByUsername(String username) {
        User user = userRepository.findByEmail(username);
        UserPrincipal userPrincipal = new UserPrincipal();
        if (null != user) {
            Set<String> authorities = new HashSet<>();
            if (null != user.getRoles())
                authorities.add(user.getRoles().getRoleName());
            userPrincipal.setUserId((long) user.getId());
            userPrincipal.setUsername(user.getEmail());
            userPrincipal.setPassword(user.getPassword());
            userPrincipal.setAuthorities(authorities);
        }
        return userPrincipal;
    }

    @Override
    public User findUserByResetPasswordToken(String token) {
        return userRepository.findUserByResetPasswordToken(token);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    @Override
    public void updateResetPasswordToken(String token, String email) throws CustormException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new CustormException(Message.EMAIL_NOT_FOUND);
        }
    }

    @Override
    public List<User> findUserByRoleName(String roleName) {
        return userRepository.findUserByRoleName(roleName);
    }

    @Override
    public List<WaitingUser> findWaittingUser() {
        List<User> waitingUsers = userRepository.findWaitingUser();
        List<WaitingUser> result = waitingUsers.stream().map(user -> new WaitingUser(user)).collect(Collectors.toList());
        return result;
    }

    @Override
    public void deleteUser(int idUser) throws CustormException {
        User user = userRepository.findById(idUser).orElse(null);
        if (user != null) {
            user.setIsDeleted(1);
            userRepository.save(user);
        }else{
            throw new CustormException(Message.INVALID_USER);
        }
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void denyUser(int[] userIds) throws CustormException {
        for (int id : userIds) {
            User user = userRepository.findById(id).orElse(null);
            if (user == null || user.getRoles() != null) {
                throw new CustormException(Message.INVALID_USER);
            }
            userRepository.delete(user);
        }
    }

    @Override
    public void acceptUsers(int[] userIds) throws CustormException {
        for (int id : userIds) {
            User user = userRepository.findById(id).orElse(null);
            if (user == null || user.getRoles() != null) {
                throw new CustormException(Message.INVALID_USER);
            }
        }
        for (int id : userIds) {
            User user = userRepository.findById(id).orElse(null);
            Role role = roleRepository.findRoleByRoleName("ROLE_USER");
            user.setRoles(role);
            userRepository.save(user);
        }
    }
}