package org.freezing.naive.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.freezing.naive.dto.GetOwnReservationsOutDto;
import org.freezing.naive.dto.GetReservationHistoryOutDto;
import org.freezing.naive.dto.Reservation;

@Mapper
public interface ReservationRepository {
    List<GetReservationHistoryOutDto> getReservationHistory(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("skip") Integer skip, @Param("limit") Integer limit);
    List<GetOwnReservationsOutDto> getOwnReservation(@Param("employeeId") Integer employeeId);
    void reserve(Reservation reservation);

    void cancel(@Param("reservationId") Integer reservationId, @Param("userId") Integer userId);
    Reservation findById(@Param("reservationId") Integer reservationId);

    void setStatus(@Param("status") String status, @Param("reservationId") Integer reservationId);
	void extend(Reservation newReservation);
	
	Reservation findInTime(@Param("seatId") Integer seatId);
	Reservation findByExtendedFromReservationId(@Param("extendedFromReservationId") Integer extendedFromReservationId);
}
