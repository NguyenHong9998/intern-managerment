package cnpm.doan.service;

import cnpm.doan.domain.ProjectDomain;
import cnpm.doan.entity.MemberProject;
import cnpm.doan.entity.Project;
import cnpm.doan.entity.User;
import cnpm.doan.repository.MemberProjectRepository;
import cnpm.doan.repository.ProjectRepository;
import cnpm.doan.repository.UserRepository;
import cnpm.doan.util.CustormException;
import cnpm.doan.util.DatetimeUtils;
import cnpm.doan.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> getProjectByUserId(long userId) {
        List<MemberProject> memberProjects = memProRepository.findAll();
        return memberProjects.stream()
                .filter(t -> t.getUser().getId() == userId).map(t -> t.getProject())
                .collect(Collectors.toList());
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
    public void deleteProject(int idProject) {
        projectRepository.deleteProject(idProject);
    }
}
