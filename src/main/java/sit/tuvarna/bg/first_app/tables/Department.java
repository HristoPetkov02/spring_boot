package sit.tuvarna.bg.first_app.tables;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Department")
    private Long id;

    @Column(name = "Department_Code", length = 10)
    private String departmentCode;

    @Column(name = "Department_Name", length = 50, nullable = false)
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "Faculty_ID")
    private Faculty faculty;
}
