package org.freezing.naive.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.freezing.naive.dto.GetAvailableSeatsOutDto;
import org.freezing.naive.dto.Seat;
import org.freezing.naive.dto.SeatOutDto;

@Mapper
public interface SeatRepository {
    List<SeatOutDto> getSeatsByFloorId(@Param("floorId") Integer floorId);
    List<GetAvailableSeatsOutDto> getAvailableSeats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, 
    		@Param("skip") Integer skip,
    		@Param("limit") Integer limit);
    
    Seat findById(@Param("id") Integer id);
}
