package sit.tuvarna.bg.first_app.tables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Faculties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Faculty")
    private Long id;

    @Column(name = "Faculty_Code", length = 10)
    private String facultyCode;

    @Column(name = "Faculty_Name", length = 50, nullable = false)
    private String facultyName;
}
