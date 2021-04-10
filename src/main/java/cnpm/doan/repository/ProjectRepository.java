package cnpm.doan.repository;

import cnpm.doan.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Modifying
    @Query(value = "delete from project p where p.id = ?1", nativeQuery = true)
    void deleteProject(int idProject);

}
