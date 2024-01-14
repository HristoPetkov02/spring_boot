package sit.tuvarna.bg.first_app.tables;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Collages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Collage")
    private Long id;

    @Column(name = "Collage_Name", length = 50, nullable = false)
    private String collegeName;
}
