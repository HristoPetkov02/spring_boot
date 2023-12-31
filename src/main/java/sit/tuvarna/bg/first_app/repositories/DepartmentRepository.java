package sit.tuvarna.bg.first_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tuvarna.bg.first_app.tables.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
