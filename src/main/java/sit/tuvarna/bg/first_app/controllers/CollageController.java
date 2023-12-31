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
import sit.tuvarna.bg.first_app.services.CollageService;
import sit.tuvarna.bg.first_app.tables.Collage;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/collages")
@RequiredArgsConstructor
public class CollageController {
    @Autowired
    private final CollageService collageService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addCollage(@RequestBody Collage collage){
        collageService.addCollage(collage);
        return ResponseEntity.ok("Collage added successfully");
    }

    @PostMapping("/add/excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addCollagesFromExcel(@RequestParam("file") MultipartFile file) {
        //името на параметъра трябва да е file
        try {
            List<Collage> collages = readCollagesFromExcel(file);
            collageService.addCollages(collages);
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
            @RequestBody Collage collage
    ){
        collageService.updateCollage(id,collage);
        return ResponseEntity.ok("Collage updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteCollage(@PathVariable Long id){
        collageService.deleteCollage(id);
        return ResponseEntity.ok("Collage deleted successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') ")
    public ResponseEntity<List<Collage>> getAllCollages() {
        List<Collage> collages = collageService.getAllCollages();
        return ResponseEntity.ok(collages);
    }




    //MultipartFile е spring class за обработка на файлове
    private List<Collage> readCollagesFromExcel(MultipartFile file) throws IOException {
        //от Apache POI създавам Workbook обект за обработване на excel файла
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //първия sheet е на 0 макар и да се казва sheet1
        Sheet sheet = workbook.getSheetAt(0);
        //DataFormatter се ползвам за да форматирам различните клетки
        DataFormatter dataFormatter = new DataFormatter();
        // columns: Collage_Name

        //чрез lambda expression имплементирам функцията интерфейс RowMapper
        List<Collage> collages = ExcelUtils.extractEntitiesFromSheet(sheet, row -> {
            Collage collage = new Collage();
            collage.setCollegeName(dataFormatter.formatCellValue(row.getCell(0)));
            return collage;
        });

        workbook.close();
        return collages;
    }
}
