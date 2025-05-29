package org.freezing.naive.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.freezing.naive.dto.GetAvailableSetsOutDto;
import org.freezing.naive.dto.GetSeatsByFloorIdOutDto;
import org.freezing.naive.dto.Seat;

@Mapper
public interface SeatRepository {
    List<GetSeatsByFloorIdOutDto> getSeatsByFloorId(@Param("floorId") Integer floorId);
    List<GetAvailableSetsOutDto> getAvailableSeats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("skip") Integer skip, @Param("limit") Integer limit);

    void setStatus(@Param("status") String status,@Param("seatId") Integer seatId);
	boolean isSeatBroken(@Param("seatId") Integer seatId);
	Seat findById(@Param("id") Integer id);
}
