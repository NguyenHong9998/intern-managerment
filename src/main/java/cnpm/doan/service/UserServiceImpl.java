package cnpm.doan.service;

import cnpm.doan.domain.UserDomain;
import cnpm.doan.domain.WaitingUser;
import cnpm.doan.entity.*;
import cnpm.doan.repository.*;
import cnpm.doan.security.JwtUtil;
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

    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Autowired
    private MemberTaskRepository memberTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
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
            userPrincipal.setUserId(user.getId());
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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<WaitingUser> findWaittingUser() {
        List<User> waitingUsers = userRepository.findAll();
        List<WaitingUser> result = waitingUsers.stream().filter(t -> t.getRoles() == null ? true : false).map(user -> new WaitingUser(user)).collect(Collectors.toList());
        return result;
    }

    @Override
    public void deleteUser(int idUser) throws CustormException {

        User user = userRepository.findById(idUser).orElse(null);
        if (user != null) {
            if (user.getRoles().getRoleName().equals("ROLE_MANAGER")) {
                List<Project> projects = projectRepository.findAll().stream().filter(t -> {
                    if (t.getManager() == null) {
                        return false;
                    }
                    return t.getManager().getId() == user.getId();
                })
                        .map(t -> {
                            t.setManager(null);
                            return t;
                        }).collect(Collectors.toList());
                projectRepository.saveAll(projects);
                user.setIsDeleted(1);
                userRepository.save(user);
                return;
            }
            List<MemberTask> memberTasks = memberTaskRepository.findAll().stream().filter(t -> t.getUser().getId() == user.getId()).collect(Collectors.toList());
            for (MemberTask memberTask : memberTasks) {
                if (!memberTask.getTask().isDone()) {
                    throw new CustormException(Message.EXIST_NOT_DONE_TASK);
                }
            }
            List<MemberProject> projects = memberProjectRepository.findAll();
            projects.stream().filter(t -> t.getUser().getId() == idUser)
                    .forEach(t -> {
                        t.setUser(null);
                        memberProjectRepository.save(t);
                    });
            user.setIsDeleted(1);
            userRepository.save(user);
        } else {
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

    @Override
    public List<UserDomain> getUserOfManager() {
        List<Integer> projectIds = projectRepository.findAll().stream().filter(t -> {
            if (t.getManager() == null) {
                return false;
            }
            return t.getManager().getId() == jwtUtil.getCurrentUser().getUserId();
        }).map(t -> t.getId()).collect(Collectors.toList());

        List<UserDomain> domain = memberProjectRepository.findAll().stream().filter(t ->
                projectIds.contains(t.getProject().getId())
        ).map(t -> t.getUser()).map(x -> new UserDomain(x)).collect(Collectors.toList());
        return domain;
    }
}