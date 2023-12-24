package sit.tuvarna.bg.first_app.tables;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Room")
    private Long id;

    @Column(name = "Room_Number")
    private int roomNumber;

    @Column(name = "Phone", length = 14, nullable = false)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "Building_ID")
    private Building building;
}
