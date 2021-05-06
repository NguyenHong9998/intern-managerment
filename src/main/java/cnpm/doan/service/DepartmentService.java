package cnpm.doan.service;

import cnpm.doan.entity.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> findAllDepartment();
    Department findById(int id);
}
