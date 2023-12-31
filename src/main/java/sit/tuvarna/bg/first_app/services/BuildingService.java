package sit.tuvarna.bg.first_app.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.repositories.BuildingRepository;
import sit.tuvarna.bg.first_app.tables.Building;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingService {
    @Autowired
    private final BuildingRepository buildingRepository;

    public void addBuilding(Building building) {
        buildingRepository.save(building);
    }

    public void addBuildings(List<Building> buildings) {
        buildingRepository.saveAll(buildings);
    }

    public void deleteBuilding(Long id) {
        buildingRepository.deleteById(id);
    }

    public void updateBuilding(Long id, Building building){
        Building existingBuilding = buildingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Building not found with id: " + id));

        //ако полетата са празни то няма да се променят
        if (building.getBuildingName() != null){
            existingBuilding.setBuildingName(building.getBuildingName());
        }

        buildingRepository.save(existingBuilding);
    }

    public List<Building> getAllBuildings() {
        return buildingRepository.findAll();
    }
}
