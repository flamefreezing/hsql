package org.freezing.naive.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.freezing.naive.dto.GetOwnReservationsOutDto;
import org.freezing.naive.dto.GetReservationHistoryOutDto;
import org.freezing.naive.dto.Reservation;
import org.freezing.naive.dto.ReserveInDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<GetOwnReservationsOutDto> getOwnReservations(Integer employeeId) {

		if (employeeId == null) {
			throw new BusinessException("employeeId is required");
		}

		System.out.println(employeeId);
		return reservationRepository.getOwnReservations(employeeId);
	}
    
    public List<GetReservationHistoryOutDto> getReservationHistory(String startDate, String endDate, Integer skip,
			Integer limit) {

		if (startDate == null || endDate == null) {
			throw new BusinessException("startDate and endDate are required");
		}

		if (!startDate.matches("\\d{4}-\\d{2}-\\d{2}") || !endDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
			throw new BusinessException("startDate and endDate must be in the format YYYY-MM-DD");
		}

		LocalDate startLocalDate = LocalDate.parse(startDate);
		LocalDate endLocalDate = LocalDate.parse(endDate);

		if (endLocalDate.isBefore(startLocalDate)) {
			throw new BusinessException("endDate must not be earlier than startDate");
		}

		LocalDateTime startDateTime = startLocalDate.atStartOfDay();
		LocalDateTime endDateTime = endLocalDate.atStartOfDay().plusDays(1);

		if (skip == null) {
			skip = 0;
		}
		if (limit == null) {
			limit = 5;
		}

		return reservationRepository.getReservationHistory(startDateTime, endDateTime, skip, limit);
	}

	public void reserve(ReserveInDto reserveInDto, int employeeId) {
		// TODO Auto-generated method stub

		if (!reserveInDto.getDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
			throw new BusinessException("Date must be in the format yyyy-MM-dd");
		}

		if (!reserveInDto.getStartTime().matches("\\d{2}:\\d{2}")
				|| !reserveInDto.getEndTime().matches("\\d{2}:\\d{2}")) {
			throw new BusinessException("Start time and end time must be in the format hh:mm");
		}

		LocalDate date = LocalDate.parse(reserveInDto.getDate());

		LocalTime startTime = LocalTime.parse(reserveInDto.getStartTime());
		LocalTime endTime = LocalTime.parse(reserveInDto.getEndTime());

		if (!endTime.isAfter(startTime)) {
			throw new BusinessException("End time must be after start time");
		}

		LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
		LocalDateTime endDateTime = LocalDateTime.of(date, endTime);

		if (!startDateTime.isAfter(LocalDateTime.now())) {
			throw new BusinessException("Start time must be after current time");
		}
		
		if(reservationRepository.isConflictTime(startDateTime, endDateTime)) {
			throw new BusinessException("Conflict Time");
		}

		var reservation = new Reservation();
		reservation.setStatus("PENDING");
		reservation.setStartTime(startDateTime);
		reservation.setEndTime(endDateTime);
		reservation.setCreatedAt(LocalDateTime.now());
		reservation.setEmployeeId(employeeId);
		reservation.setSeatId(reserveInDto.getSeatId());

		reservationRepository.reserve(reservation);

	}

}
