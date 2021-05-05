package cnpm.doan.service;

import cnpm.doan.domain.GetAllProjectDomain;
import cnpm.doan.domain.ProjectByUserIdDomain;
import cnpm.doan.domain.ProjectDomain;
import cnpm.doan.domain.ResponeDomain;
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
                    GetAllProjectDomain domain = new GetAllProjectDomain();
                    domain.setProjectId(String.valueOf(t.getId()));
                    domain.setDescription(t.getDescription());
                    domain.setDueDate(t.getDueDate().toString());
                    domain.setTitle(t.getTitle());
                    domain.setManagerName(t.getManager().getName());
                    return domain;
                }
        ).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<ProjectByUserIdDomain> getProjectByUserId(long userId) {
        List<MemberProject> memberProjects = memProRepository.findAll();
        System.out.println("aaaaaaaaaaaaaaaa4: "+ userId);

        List<ProjectByUserIdDomain> result = memberProjects.stream().filter(t -> t.getUser().getId() == userId)
                .map(t -> {
                    ProjectByUserIdDomain domain = new ProjectByUserIdDomain();
                    domain.setProjectId(String.valueOf(t.getProject().getId()));
                    domain.setDescription(t.getProject().getDescription());
                    domain.setUserId(String.valueOf(userId));
                    domain.setDueDate(t.getProject().getDueDate().toString());
                    domain.setTitle(t.getProject().getTitle());
                    domain.setManagerName(t.getProject().getManager().getName());
                    return domain;
                }).collect(Collectors.toList());
        System.out.println("aaaaaaaaaaaaaaaa5: "+ userId);

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
        project.setIsDeleted(1);
        projectRepository.save(project);
    }
}
