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
import sit.tuvarna.bg.first_app.services.RoomService;
import sit.tuvarna.bg.first_app.tables.Building;
import sit.tuvarna.bg.first_app.tables.Room;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    @Autowired
    private final RoomService roomService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addRoom(@RequestBody Room room){
        roomService.addRoom(room);
        return ResponseEntity.ok("Room added successfully");
    }

    @PostMapping("/add/excel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addRoomsFromExcel(@RequestParam("file") MultipartFile file) {
        //името на параметъра трябва да е file
        try {
            List<Room> rooms = readRoomsFromExcel(file);
            roomService.addRooms(rooms);
            return ResponseEntity.ok("Rooms added successfully from Excel");
        }
        catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing Excel file");
        }
    }

    //{id} я мапвам като path variable и ще я взема за Long id
    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateRoom(
            @PathVariable Long id,
            @RequestBody Room room
    ) {
        roomService.updateRoom(id,room);
        return ResponseEntity.ok("Room updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id){
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') ")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms=roomService.getAllRooms();
        return  ResponseEntity.ok(rooms);
    }



    //MultipartFile е spring class за обработка на файлове
    private List<Room> readRoomsFromExcel(MultipartFile file) throws IOException {
        //от Apache POI създавам Workbook обект за обработване на excel файла
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //първия sheet е на 0 макар и да се казва sheet1
        Sheet sheet = workbook.getSheetAt(0);
        //DataFormatter се ползвам за да форматирам различните клетки
        DataFormatter dataFormatter = new DataFormatter();
        // columns: Room_Number, Phone, Building_ID

        List<Room> rooms = ExcelUtils.extractEntitiesFromSheet(sheet, row -> {
            Room room = new Room();
            room.setRoomNumber(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(0))));
            room.setPhone(dataFormatter.formatCellValue(row.getCell(1)));

            Building building = new Building();

            String buildingIdString = dataFormatter.formatCellValue(row.getCell(2));
            if (!buildingIdString.isEmpty()) {
                building.setId(Long.parseLong(buildingIdString));
            }
            room.setBuilding(building);

            return room;
        });

        workbook.close();
        return rooms;
    }
}
