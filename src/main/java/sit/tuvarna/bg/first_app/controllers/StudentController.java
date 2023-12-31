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
import sit.tuvarna.bg.first_app.services.StudentService;
import sit.tuvarna.bg.first_app.tables.Collage;
import sit.tuvarna.bg.first_app.tables.Department;
import sit.tuvarna.bg.first_app.tables.Room;
import sit.tuvarna.bg.first_app.tables.Student;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    @Autowired
    private final StudentService studentService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addStudent(@RequestBody Student student){
        studentService.addStudent(student);
        return ResponseEntity.ok("Student added successfully");
    }

    @PostMapping("/add/excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addStudentsFromExcel(@RequestParam("file") MultipartFile file) {
        //името на параметъра трябва да е file
        try {
            List<Student> students = readStudentsFromExcel(file);
            studentService.addStudents(students);
            return ResponseEntity.ok("Students added successfully from Excel");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing Excel file");
        }
    }

    //{id} я мапвам като path variable и ще я взема за Long id
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student
    ) {
        studentService.updateStudent(id,student);
        return ResponseEntity.ok("Student updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') ")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students=studentService.getAllStudents();
        return  ResponseEntity.ok(students);
    }

    //MultipartFile е spring class за обработка на файлове
    private List<Student> readStudentsFromExcel(MultipartFile file) throws IOException {
        //от Apache POI създавам Workbook обект за обработване на excel файла
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //първия sheet е на 0 макар и да се казва sheet1
        Sheet sheet = workbook.getSheetAt(0);
        //DataFormatter се ползвам за да форматирам различните клетки
        DataFormatter dataFormatter = new DataFormatter();
        // columns: FN, Department_ID, Collage_ID, Room_ID

        List<Student> students = ExcelUtils.extractEntitiesFromSheet(sheet, row -> {
           Student student= new Student();
           student.setFn(dataFormatter.formatCellValue(row.getCell(0)));

            Department department = new Department();
            Collage collage = new Collage();
            Room room = new Room();

            String departmentIdString = dataFormatter.formatCellValue(row.getCell(1));
            if (!departmentIdString.isEmpty()) {
                department.setId(Long.parseLong(departmentIdString));
            }
            student.setDepartment(department);

            String collageIdString = dataFormatter.formatCellValue(row.getCell(2));
            if (!collageIdString.isEmpty()) {
                collage.setId(Long.parseLong(collageIdString));
            }
            student.setCollage(collage);

            String roomIdString = dataFormatter.formatCellValue(row.getCell(3));
            if (!roomIdString.isEmpty()) {
                room.setId(Long.parseLong(roomIdString));
            }
            student.setRoom(room);

            return student;
        });

        workbook.close();
        return students;
    }
}
