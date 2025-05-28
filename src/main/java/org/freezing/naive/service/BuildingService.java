package org.freezing.naive.service;

import java.util.List;

import org.freezing.naive.dto.GetBuildingsOutDto;
import org.freezing.naive.repository.BuildingRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;

    public List<GetBuildingsOutDto> getBuildings() {
        return buildingRepository.getBuildings();
    }
}
