package cnpm.doan.service;

import cnpm.doan.entity.Department;
import cnpm.doan.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public Department findById(int id) {
        return departmentRepository.findById(id).orElse(null);
    }
}
