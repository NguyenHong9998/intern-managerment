package cnpm.doan.aspect;

import cnpm.doan.entity.LeadPermission;
import cnpm.doan.repository.LeaderPermissionRepository;
import cnpm.doan.security.JwtUtil;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.Message;
import cnpm.doan.util.PermisstionType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class PermissionAspectImpl {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private LeaderPermissionRepository leaderPermissionRepository;

    @Around("@annotation(permissionAspect)")
    public Object validateAspect(ProceedingJoinPoint joinPoint, PermissionAspect permissionAspect) throws Throwable {
        int userId = jwtUtil.getCurrentUser().getUserId();
        List<LeadPermission> leadPermissions = leaderPermissionRepository.findAllByUserId(userId);
        List<String> permissionString = leadPermissions.stream().map(t -> t.getPermission().getName()).collect(Collectors.toList());
        PermisstionType[] permisstionTypes = permissionAspect.permission();
        for (PermisstionType permisstionType : permisstionTypes) {
            if (permissionString.contains(permisstionType.getPermissionName())) {
                throw new CustormException(Message.HAVE_NOT_PERMISSION);
            }
        }
        return joinPoint.proceed();
    }
}
