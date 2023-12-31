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
import sit.tuvarna.bg.first_app.services.BuildingService;
import sit.tuvarna.bg.first_app.tables.Building;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/buildings")
@RequiredArgsConstructor
public class BuildingController {
    @Autowired
    private final BuildingService buildingService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addBuilding(@RequestBody Building building){
        buildingService.addBuilding(building);
        return ResponseEntity.ok("Building added successfully");
    }

    @PostMapping("/add/excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addBuildingFromExcel(@RequestParam("file") MultipartFile file) {
        //името на параметъра трябва да е file
        try {
            List<Building> buildings = readBuildingsFromExcel(file);
            buildingService.addBuildings(buildings);
            return ResponseEntity.ok("Buildings added successfully from Excel");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing Excel file");
        }
    }

    //{id} я мапвам като path variable и ще я взема за Long id
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateBuilding(
            @PathVariable Long id,
            @RequestBody Building building
    ){
        buildingService.updateBuilding(id,building);
        return ResponseEntity.ok("Building updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteBuilding(@PathVariable Long id){
        buildingService.deleteBuilding(id);
        return ResponseEntity.ok("Building deleted successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') ")
    public ResponseEntity<List<Building>> getAllBuildings() {
        List<Building> buildings = buildingService.getAllBuildings();
        return ResponseEntity.ok(buildings);
    }




    //MultipartFile е spring class за обработка на файлове
    private List<Building> readBuildingsFromExcel(MultipartFile file) throws IOException {
        //от Apache POI създавам Workbook обект за обработване на excel файла
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //първия sheet е на 0 макар и да се казва sheet1
        Sheet sheet = workbook.getSheetAt(0);
        //DataFormatter се ползвам за да форматирам различните клетки
        DataFormatter dataFormatter = new DataFormatter();
        // columns: Building_Name

        //чрез lambda expression имплементирам функцията интерфейс RowMapper
        List<Building> buildings = ExcelUtils.extractEntitiesFromSheet(sheet, row -> {
            Building building = new Building();
            building.setBuildingName(dataFormatter.formatCellValue(row.getCell(0)));
            return building;
        });

        workbook.close();
        return buildings;
    }
}
