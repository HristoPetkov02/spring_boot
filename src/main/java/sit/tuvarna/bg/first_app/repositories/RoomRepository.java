package sit.tuvarna.bg.first_app.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import sit.tuvarna.bg.first_app.tables.Room;


public interface RoomRepository extends JpaRepository<Room, Long> {
}
