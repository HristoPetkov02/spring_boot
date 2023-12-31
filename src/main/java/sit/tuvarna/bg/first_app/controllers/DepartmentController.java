package sit.tuvarna.bg.first_app.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.tuvarna.bg.first_app.ExcelUtils;
import sit.tuvarna.bg.first_app.services.DepartmentService;
import sit.tuvarna.bg.first_app.tables.Department;
import sit.tuvarna.bg.first_app.tables.Faculty;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    @Autowired
    private final DepartmentService departmentService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addDepartment(@RequestBody Department department) {
        departmentService.addDepartment(department);
        return ResponseEntity.ok("Department added successfully");
    }

    @PostMapping("/add/excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addDepartmentsFromExcel(@RequestParam("file") MultipartFile file) {
        //името на параметъра трябва да е file
        try {
            List<Department> departments = readDepartmentsFromExcel(file);
            departmentService.addDepartments(departments);
            return ResponseEntity.ok("Departments added successfully from Excel");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing Excel file");
        }
    }

    //{id} я мапвам като path variable и ще я взема за Long id
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department department
    ) {
        departmentService.updateDepartment(id, department);
        return ResponseEntity.ok("Department updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') ")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }



    //MultipartFile е spring class за обработка на файлове
    private List<Department> readDepartmentsFromExcel(MultipartFile file) throws IOException {
        //от Apache POI създавам Workbook обект за обработване на excel файла
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //първия sheet е на 0 макар и да се казва sheet1
        Sheet sheet = workbook.getSheetAt(0);
        //DataFormatter се ползвам за да форматирам различните клетки
        DataFormatter dataFormatter = new DataFormatter();
        // columns: Department_Code, Department_Name, Faculty_ID

        //чрез lambda expression имплементирам функцията интерфейс RowMapper
        List<Department> departments = ExcelUtils.extractEntitiesFromSheet(sheet, row -> {
            Department department = new Department();
            department.setDepartmentCode(dataFormatter.formatCellValue(row.getCell(0)));
            department.setDepartmentName(dataFormatter.formatCellValue(row.getCell(1)));

            Faculty faculty = new Faculty();
            String facultyIdString = dataFormatter.formatCellValue(row.getCell(2));
            if (!facultyIdString.isEmpty()) {
                faculty.setId(Long.parseLong(facultyIdString));
            }
            department.setFaculty(faculty);

            return department;
        });

        workbook.close();
        return departments;
    }
}
