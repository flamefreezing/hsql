package org.freezing.naive.service;

import lombok.RequiredArgsConstructor;
import org.freezing.naive.dto.GetFloorsOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.FloorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final FloorRepository floorRepository;

    public List<GetFloorsOutDto> getFloors(Integer buildingId) {

		if (buildingId == null) {
			throw new IllegalArgumentException("buildingId is required");
		}

		List<GetFloorsOutDto> floors = floorRepository.getFloorsByBuildingId(buildingId);
		if (floors.isEmpty()) {
			throw new BusinessException("No floors found for the building", 404);
		}
		return floors;
	}

}
