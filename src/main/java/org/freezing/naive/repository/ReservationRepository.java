package org.freezing.naive.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.freezing.naive.dto.GetReservationHistoryOutDto;

@Mapper
public interface ReservationRepository {
    List<GetReservationHistoryOutDto> getReservationHistory(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("skip") Integer skip, @Param("limit") Integer limit);
}
