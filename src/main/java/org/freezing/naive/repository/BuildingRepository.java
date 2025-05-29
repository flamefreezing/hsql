package org.freezing.naive.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.freezing.naive.dto.GetBuildingsOutDto;

@Mapper
public interface BuildingRepository {
    List<GetBuildingsOutDto> getBuildings();
}
