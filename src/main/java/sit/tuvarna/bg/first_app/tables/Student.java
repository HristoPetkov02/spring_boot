package sit.tuvarna.bg.first_app.tables;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Student")
    private Long id;

    @Column(name = "FN", length = 12, nullable = false)
    private String fn;

    @ManyToOne
    @JoinColumn(name = "Department_ID")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "Collage_ID")
    private Collage collage;

    @ManyToOne
    @JoinColumn(name = "Room_ID")
    private Room room;
}
