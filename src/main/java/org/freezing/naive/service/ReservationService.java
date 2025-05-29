package org.freezing.naive.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.freezing.naive.dto.*;
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
        List<GetReservationHistoryOutDto> reservationHistory = reservationRepository.getReservationHistory(startDateTime, endDateTime, skip, limit);
        return reservationHistory;
    }

    public List<GetOwnReservationsOutDto> getOwnReservations(Integer employeeId) {
        return reservationRepository.getOwnReservation(employeeId);
    }

    public void reserve(ReserveInDto reserveInDto, Integer userId) {
        LocalDate date = LocalDate.parse(reserveInDto.getDate());
        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.parse(reserveInDto.getStartTime()));
        LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.parse(reserveInDto.getEndTime()));

        Reservation reservation = new Reservation();
        reservation.setSeatId(reserveInDto.getSeatId());
        reservation.setEmployeeId(userId);
        reservation.setStartTime(startDateTime);
        reservation.setEndTime(endDateTime);
        reservation.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        reservation.setStatus("PENDING");

        reservationRepository.reserve(reservation);
    }

    public void cancel(Integer reservationId, Integer userId) {
        reservationRepository.cancel(reservationId, userId);
    }

	public void extend(ExtendReservationInDto extendReservationInDto, Integer userId) {
		Reservation lastReservation = reservationRepository.findById(extendReservationInDto.getReservationId());
		Reservation newReservation = new Reservation();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		LocalDateTime endLocalDateTime = LocalDateTime.parse(extendReservationInDto.getEndTime(), formatter);
		
		newReservation.setCheckInAt(endLocalDateTime.minusHours(1));
		newReservation.setCreatedAt(LocalDateTime.now());
		newReservation.setEmployeeId(userId);
		newReservation.setEndTime(endLocalDateTime);
		newReservation.setExtendedFromReservationId(extendReservationInDto.getReservationId());
		newReservation.setSeatId(lastReservation.getSeatId());
		newReservation.setStartTime(endLocalDateTime.minusHours(1));
		newReservation.setStatus("IN_USE");
		
		reservationRepository.extend(newReservation);
	}
}
