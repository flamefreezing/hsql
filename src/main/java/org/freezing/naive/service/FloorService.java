package org.freezing.naive.service;

import java.util.List;

import org.freezing.naive.dto.GetFloorsByBuildingIdOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.FloorRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FloorService {
    private final FloorRepository floorRepository;

    public List<GetFloorsByBuildingIdOutDto> getFloorsByBuildingId(Integer buildingId) {

		if (buildingId == null) {
			throw new BusinessException("buildingId is required");
		}

		return floorRepository.getFloorsByBuildingId(buildingId);
	}

}
