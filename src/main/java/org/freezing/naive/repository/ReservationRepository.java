package org.freezing.naive.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.freezing.naive.dto.GetOwnReservationsOutDto;
import org.freezing.naive.dto.GetReservationHistoryOutDto;
import org.freezing.naive.dto.Reservation;

@Mapper
public interface ReservationRepository {
    List<GetOwnReservationsOutDto> getOwnReservations(@Param("employeeId") Integer employeeId);
    List<GetReservationHistoryOutDto> getReservationHistory(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime,
    		@Param("skip") Integer skip,
    		@Param("limit") Integer limit );
	void reserve(Reservation reservation);
	boolean isConflictTime(@Param("startDateTime") LocalDateTime startDateTime,@Param("endDateTime") LocalDateTime endDateTime);
}
