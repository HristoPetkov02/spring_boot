package sit.tuvarna.bg.first_app.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.repositories.CollegeRepository;
import sit.tuvarna.bg.first_app.tables.College;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollegeService {
    @Autowired
    private final CollegeRepository collegeRepository;

    public void addCollage(College college) {
        collegeRepository.save(college);
    }

    public void addCollages(List<College> colleges) {
        collegeRepository.saveAll(colleges);
    }

    public void deleteCollage(Long id) {
        collegeRepository.deleteById(id);
    }

    public void updateCollage(Long id, College college) {
        College existingCollege = collegeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collage not found with id: " + id));

        //ако полетата са празни то няма да се променят
        if (college.getCollegeName() != null) {
            existingCollege.setCollegeName(college.getCollegeName());
        }

        collegeRepository.save(existingCollege);
    }

    public List<College> getAllCollages() {
        return collegeRepository.findAll();
    }


    public College getCollageById(Long id) {
        return collegeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collage not found with id: " + id));
    }
}
