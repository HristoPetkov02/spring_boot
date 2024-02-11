package sit.tuvarna.bg.first_app.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.repositories.RoomRepository;
import sit.tuvarna.bg.first_app.tables.Room;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    @Autowired
    private final RoomRepository roomRepository;

    public void addRoom(Room room) {
        roomRepository.save(room);
    }

    public void addRooms(List<Room> rooms) {
        roomRepository.saveAll(rooms);
    }


    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public void updateRoom(Long id, Room room) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));

        if (room.getRoomNumber() != 0)
            existingRoom.setRoomNumber(room.getRoomNumber());

        if (room.getPhone() != null)
            existingRoom.setPhone(room.getPhone());

        if (room.getBuilding() != null)
            existingRoom.setBuilding(room.getBuilding());

        roomRepository.save(existingRoom);
    }

    public List<Room> getAllRooms() {
        return  roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
    }
}
