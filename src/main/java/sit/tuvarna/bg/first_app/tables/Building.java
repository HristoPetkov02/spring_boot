package sit.tuvarna.bg.first_app.tables;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Buildings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Building")
    private Long id;

    @Column(name = "Building_Name", length = 32)
    private String buildingName;
}
