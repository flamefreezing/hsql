package org.freezing.naive.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.freezing.naive.dto.GetFloorsByBuildingIdOutDto;

@Mapper
public interface FloorRepository {
    List<GetFloorsByBuildingIdOutDto> getFloorsByBuildingId(@Param("buildingId") Integer buildingId);
}
