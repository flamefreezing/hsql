package org.freezing.naive.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.freezing.naive.dto.GetReservationHistoryOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<GetReservationHistoryOutDto> getReservationHistory(String startDate, String endDate, Integer skip, Integer limit) {
		LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(endDate).atStartOfDay().plusDays(1);
		System.out.println(startDateTime.getDayOfMonth());
		System.out.println(endDateTime.getDayOfMonth());
        List<GetReservationHistoryOutDto> reservationHistory = reservationRepository.getReservationHistory(startDateTime, endDateTime, skip, limit);
        if (reservationHistory.isEmpty()) {
            throw new BusinessException("No reservation history found", 404);
        }
        return reservationHistory;
    }
}
