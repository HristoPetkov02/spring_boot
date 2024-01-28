package sit.tuvarna.bg.first_app.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.repositories.FacultyRepository;
import sit.tuvarna.bg.first_app.tables.Faculty;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyService {
    @Autowired
    private final FacultyRepository facultyRepository;

    public void addFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    public void addFaculties(List<Faculty> faculties) {
        facultyRepository.saveAll(faculties);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public void updateFaculty(Long id, Faculty faculty) {
        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found with id: " + id));

        //ако полетата са празни то няма да се променят
        if (faculty.getFacultyCode() != null) {
            existingFaculty.setFacultyCode(faculty.getFacultyCode());
        }

        if (faculty.getFacultyName() != null) {
            existingFaculty.setFacultyName(faculty.getFacultyName());
        }

        facultyRepository.save(existingFaculty);
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found with id: " + id));
    }

}
