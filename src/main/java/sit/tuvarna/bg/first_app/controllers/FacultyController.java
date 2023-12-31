package sit.tuvarna.bg.first_app.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.tuvarna.bg.first_app.ExcelUtils;
import sit.tuvarna.bg.first_app.services.FacultyService;
import sit.tuvarna.bg.first_app.tables.Faculty;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/faculties")
@RequiredArgsConstructor
public class FacultyController {
    @Autowired
    private final FacultyService facultyService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addFaculty(@RequestBody Faculty faculty) {
        facultyService.addFaculty(faculty);
        return ResponseEntity.ok("Faculty added successfully");
    }

    @PostMapping("/add/excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addFacultiesFromExcel(@RequestParam("file") MultipartFile file) {
        //името на параметъра трябва да е file
        try {
            List<Faculty> faculties = readFacultiesFromExcel(file);
            facultyService.addFaculties(faculties);
            return ResponseEntity.ok("Faculties added successfully from Excel");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing Excel file");
        }
    }

    //{id} я мапвам като path variable и ще я взема за Long id
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateFaculty(
            @PathVariable Long id,
            @RequestBody Faculty faculty
    ) {
        facultyService.updateFaculty(id, faculty);
        return ResponseEntity.ok("Faculty updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok("Faculty deleted successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') ")
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyService.getAllFaculties();
        return ResponseEntity.ok(faculties);
    }



    //MultipartFile е spring class за обработка на файлове
    private List<Faculty> readFacultiesFromExcel(MultipartFile file) throws IOException {
        //от Apache POI създавам Workbook обект за обработване на excel файла
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //първия sheet е на 0 макар и да се казва sheet1
        Sheet sheet = workbook.getSheetAt(0);
        //DataFormatter се ползвам за да форматирам различните клетки
        DataFormatter dataFormatter = new DataFormatter();
        // columns: Faculty_Code, Faculty_Name

        //чрез lambda expression имплементирам функцията интерфейс RowMapper
        List<Faculty> faculties = ExcelUtils.extractEntitiesFromSheet(sheet, row -> {
            Faculty faculty = new Faculty();
            faculty.setFacultyCode(dataFormatter.formatCellValue(row.getCell(0)));
            faculty.setFacultyName(dataFormatter.formatCellValue(row.getCell(1)));
            return faculty;
        });

        workbook.close();
        return faculties;
    }
}
