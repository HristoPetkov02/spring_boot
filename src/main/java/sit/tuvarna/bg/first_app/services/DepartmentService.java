package sit.tuvarna.bg.first_app.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.repositories.DepartmentRepository;
import sit.tuvarna.bg.first_app.tables.Department;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    @Autowired
    private final DepartmentRepository departmentRepository;

    public void addDepartment(Department department) {
        departmentRepository.save(department);
    }

    public void addDepartments(List<Department> departments) {
        departmentRepository.saveAll(departments);
    }


    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public void updateDepartment(Long id, Department department) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        if (department.getDepartmentCode() != null) {
            existingDepartment.setDepartmentCode(department.getDepartmentCode());
        }

        if (department.getDepartmentName() != null) {
            existingDepartment.setDepartmentName(department.getDepartmentName());
        }

        if (department.getFaculty() != null) {
            existingDepartment.setFaculty(department.getFaculty());
        }



        departmentRepository.save(existingDepartment);
    }



    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }
}
