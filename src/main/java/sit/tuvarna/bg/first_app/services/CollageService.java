package sit.tuvarna.bg.first_app.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.repositories.CollageRepository;
import sit.tuvarna.bg.first_app.tables.Collage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollageService {
    @Autowired
    private final CollageRepository collegeRepository;

    public void addCollage(Collage collage) {
        collegeRepository.save(collage);
    }

    public void addCollages(List<Collage> collages) {
        collegeRepository.saveAll(collages);
    }

    public void deleteCollage(Long id) {
        collegeRepository.deleteById(id);
    }

    public void updateCollage(Long id, Collage collage) {
        Collage existingCollage = collegeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collage not found with id: " + id));

        //ако полетата са празни то няма да се променят
        if (collage.getCollegeName() != null) {
            existingCollage.setCollegeName(collage.getCollegeName());
        }

        collegeRepository.save(existingCollage);
    }

    public List<Collage> getAllCollages() {
        return collegeRepository.findAll();
    }
}
