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
import sit.tuvarna.bg.first_app.services.CollegeService;
import sit.tuvarna.bg.first_app.tables.College;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/colleges")
@RequiredArgsConstructor
public class CollegeController {
    @Autowired
    private final CollegeService collegeService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addCollage(@RequestBody College college){
        collegeService.addCollage(college);
        return ResponseEntity.ok("Collage added successfully");
    }

    @PostMapping("/add/excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addCollagesFromExcel(@RequestParam("file") MultipartFile file) {
        //името на параметъра трябва да е file
        try {
            List<College> colleges = readCollagesFromExcel(file);
            collegeService.addCollages(colleges);
            return ResponseEntity.ok("Collages added successfully from Excel");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing Excel file");
        }
    }

    //{id} я мапвам като path variable и ще я взема за Long id
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateCollage(
            @PathVariable Long id,
            @RequestBody College college
    ){
        collegeService.updateCollage(id, college);
        return ResponseEntity.ok("Collage updated successfully");
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteCollage(@PathVariable Long id){
        collegeService.deleteCollage(id);
        return ResponseEntity.ok("Collage deleted successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') ")
    public ResponseEntity<List<College>> getAllCollages() {
        List<College> colleges = collegeService.getAllCollages();
        return ResponseEntity.ok(colleges);
    }

    @GetMapping("/{id}/find")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') ")
    public ResponseEntity<College> getCollageById(@PathVariable Long id) {
        College college = collegeService.getCollageById(id);
        return ResponseEntity.ok(college);
    }


    //MultipartFile е spring class за обработка на файлове
    private List<College> readCollagesFromExcel(MultipartFile file) throws IOException {
        //от Apache POI създавам Workbook обект за обработване на excel файла
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //първия sheet е на 0 макар и да се казва sheet1
        Sheet sheet = workbook.getSheetAt(0);
        //DataFormatter се ползвам за да форматирам различните клетки
        DataFormatter dataFormatter = new DataFormatter();
        // columns: Collage_Name

        //чрез lambda expression имплементирам функцията интерфейс RowMapper
        List<College> colleges = ExcelUtils.extractEntitiesFromSheet(sheet, row -> {
            College college = new College();
            college.setCollegeName(dataFormatter.formatCellValue(row.getCell(0)));
            return college;
        });

        workbook.close();
        return colleges;
    }
}
