package cnpm.doan.service;

import cnpm.doan.domain.*;
import cnpm.doan.entity.MemberProject;
import cnpm.doan.entity.Project;
import cnpm.doan.entity.Task;
import cnpm.doan.entity.User;
import cnpm.doan.repository.MemberProjectRepository;
import cnpm.doan.repository.ProjectRepository;
import cnpm.doan.repository.TaskRepository;
import cnpm.doan.repository.UserRepository;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.DatetimeUtils;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberProjectRepository memProRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<GetAllProjectDomain> getAllProject() {
        List<Project> projects = projectRepository.findAll();
        List<GetAllProjectDomain> result = projects.stream().map(
                t -> {
                    List<MemberProject> memberProjects = memProRepository.findMemberProjectByProjectId(t.getId());
                    List<UserProject> userProjects = memberProjects.stream().map(x -> {
                        UserProject userProject = new UserProject();
                        userProject.setId(x.getUser().getId());
                        userProject.setName(x.getUser().getName());
                        return userProject;
                    }).collect(Collectors.toList());
                    GetAllProjectDomain domain = new GetAllProjectDomain();
                    domain.setProjectId(t.getId());
                    domain.setDescription(t.getDescription());
                    domain.setDueDate(t.getDueDate().toString());
                    domain.setTitle(t.getTitle());
                    if (t.getManager() != null) {
                        ManagerInforDomain manager = new ManagerInforDomain(t.getManager().getId(), t.getManager().getName(), t.getManager().getEmail());
                        domain.setManagerName(manager);
                    }
                    domain.setStartDate(t.getStartDate().toString());
                    domain.setUserAssignee(userProjects);
                    return domain;
                }
        ).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<ProjectByUserIdDomain> getProjectByUserId(long userId) {
        List<MemberProject> memberProjects = memProRepository.findAll();
        List<ProjectByUserIdDomain> result = memberProjects.stream().filter(t -> t.getUser().getId() == userId)
                .map(t -> {
                    ProjectByUserIdDomain domain = new ProjectByUserIdDomain();
                    domain.setProjectId(t.getProject().getId());
                    domain.setDescription(t.getProject().getDescription());
                    domain.setUserId(String.valueOf(userId));
                    domain.setDueDate(t.getProject().getDueDate().toString());
                    domain.setTitle(t.getProject().getTitle());
                    if (t.getProject().getManager() != null) {
                        ManagerInforDomain manager = new ManagerInforDomain(t.getProject().getManager().getId(), t.getProject().getManager().getName(), t.getProject().getManager().getEmail());
                        domain.setManagerName(manager);
                    }
                    return domain;
                }).collect(Collectors.toList());
        return result;
    }

    @Override
    public void saveProject(ProjectDomain domain) throws CustormException {
        User admin = userRepository.findById(domain.getIdOfAdmin()).orElse(null);
        Project project = new Project();
        project.setManager(admin);
        project.setDescription(domain.getDescription());
        Date date = DatetimeUtils.convertStringToDateOrNull(domain.getDueDate(), DatetimeUtils.YYYYMMDD);
        if (date == null || date.before(date)) {
            throw new CustormException(Message.INVALID_DATE);
        }
        project.setStartDate(new Date());
        project.setDueDate(date);
        project.setTitle(domain.getTitle());
        projectRepository.save(project);
    }

    @Override
    public Project findProjectById(int id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteProject(int idProject) throws CustormException {
        Project project = projectRepository.findById(idProject).orElse(null);
        if (project == null) {
            throw new CustormException(Message.DATA_NOT_EXIST);
        }

        List<Task> tasks = taskRepository.findAllByProjectId(idProject);
        Task task = tasks.stream().filter(t -> t.isDone() == false).findFirst().orElse(null);
        if (task != null) {
            throw new CustormException(Message.PROJECT_NOT_DONE);
        }
        List<MemberProject> memberProjects = memProRepository.findAll().stream().filter(t -> t.getProject().getId() == idProject).collect(Collectors.toList());
        for (MemberProject memberProject : memberProjects) {
            memProRepository.delete(memberProject);
        }
        project.setIsDeleted(1);
        projectRepository.save(project);
    }

    @Override
    public List<GetAllProjectDomain> findProjectByManagerId(int managerId) {
        List<Project> projects = projectRepository.findAll().stream().filter(t -> t.getManager().getId() == managerId).collect(Collectors.toList());
        List<GetAllProjectDomain> result = projects.stream().map(
                t -> {
                    List<MemberProject> memberProjects = memProRepository.findMemberProjectByProjectId(t.getId());
                    List<UserProject> userProjects = memberProjects.stream().map(x -> {
                        UserProject userProject = new UserProject();
                        userProject.setId(x.getUser().getId());
                        userProject.setName(x.getUser().getName());
                        return userProject;
                    }).collect(Collectors.toList());
                    GetAllProjectDomain domain = new GetAllProjectDomain();
                    domain.setProjectId(t.getId());
                    domain.setDescription(t.getDescription());
                    domain.setDueDate(t.getDueDate().toString());
                    domain.setTitle(t.getTitle());
                    if (t.getManager() != null) {
                        ManagerInforDomain manager = new ManagerInforDomain(t.getManager().getId(), t.getManager().getName(), t.getManager().getEmail());
                        domain.setManagerName(manager);
                    }
                    domain.setStartDate(t.getStartDate().toString());
                    domain.setUserAssignee(userProjects);
                    return domain;
                }
        ).collect(Collectors.toList());
        return result;
    }

}
