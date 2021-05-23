package cnpm.doan.service;

import cnpm.doan.domain.GetAllProjectDomain;
import cnpm.doan.domain.ProjectByUserIdDomain;
import cnpm.doan.domain.ProjectDomain;
import cnpm.doan.entity.Project;
import cnpm.doan.util.CustormException;

import java.util.List;

public interface ProjectService {
    List<GetAllProjectDomain> getAllProject();

    List<ProjectByUserIdDomain> getProjectByUserId(long userId);

    void saveProject(ProjectDomain domain) throws CustormException;

    Project findProjectById(int id);

    void deleteProject(int idProject) throws CustormException;

    List<GetAllProjectDomain> findProjectByManagerId(int managerId);
}
