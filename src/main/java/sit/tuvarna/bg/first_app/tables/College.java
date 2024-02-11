package sit.tuvarna.bg.first_app.tables;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Colleges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_College")
    private Long id;

    @Column(name = "College_Name", length = 50, nullable = false)
    private String collegeName;
}
