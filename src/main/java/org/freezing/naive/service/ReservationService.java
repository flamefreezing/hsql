package org.freezing.naive.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.freezing.naive.dto.ExtendReservationInDto;
import org.freezing.naive.dto.GetOwnReservationsOutDto;
import org.freezing.naive.dto.GetReservationHistoryOutDto;
import org.freezing.naive.dto.Reservation;
import org.freezing.naive.dto.ReserveInDto;
import org.freezing.naive.dto.Seat;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.ReservationRepository;
import org.freezing.naive.repository.SeatRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    public List<GetReservationHistoryOutDto> getReservationHistory(String startDate, String endDate, Integer skip,
			Integer limit) {

		if (startDate == null || endDate == null) {
			throw new BusinessException("startDate and endDate are required");
		}

		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			LocalDate.parse(startDate, dateFormatter);
		} catch (BusinessException e) {
			throw new BusinessException("startDate format is incorrect. Expected format: yyyy-MM-dd");
		}

		try {
			LocalDate.parse(endDate, dateFormatter);
		} catch (BusinessException e) {
			throw new BusinessException("endDate format is incorrect. Expected format: yyyy-MM-dd");
		}

		if (LocalDate.parse(endDate).isBefore(LocalDate.parse(startDate))) {
			throw new BusinessException("endDate must not be earlier than the start date");
		}

		LocalDateTime startDateTime = LocalDate.parse(startDate).atStartOfDay();
		LocalDateTime endDateTime = LocalDate.parse(endDate).atStartOfDay().plusDays(1);

		if (skip == null) {
			skip = 0;
		}
		if (limit == null) {
			limit = 5;
		}

		List<GetReservationHistoryOutDto> reservationHistory = reservationRepository
				.getReservationHistory(startDateTime, endDateTime, skip, limit);
		return reservationHistory;
	}

    public List<GetOwnReservationsOutDto> getOwnReservations(Integer employeeId) {
        return reservationRepository.getOwnReservation(employeeId);
    }

    public void reserve(ReserveInDto reserveInDto, Integer userId) {

		if (reserveInDto.getStartTime() == null || reserveInDto.getEndTime() == null) {
			throw new BusinessException("startTime and endTime are required.");
		}

		if (!reserveInDto.getStartTime().matches("\\d{2}:\\d{2}")
				|| !reserveInDto.getEndTime().matches("\\d{2}:\\d{2}")) {
			throw new BusinessException("startTime and endTime must be in the format hh:mm.");
		}

		LocalDate date = LocalDate.parse(reserveInDto.getDate());

		LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.parse(reserveInDto.getStartTime()));

		if (startDateTime.isBefore(LocalDateTime.now())) {
			throw new BusinessException("startDateTime must be greater than or equal to now.");
		}

		LocalDateTime endDateTime = LocalDateTime.of(date, LocalTime.parse(reserveInDto.getEndTime()));

		if (!endDateTime.isAfter(startDateTime)) {
			throw new BusinessException("endTime must be after startTime.");
		}
	
		if(seatRepository.isSeatBroken(reserveInDto.getSeatId())) {
			throw new BusinessException("Seat is broken and cannot be reserved");
		}
		
		if(reservationRepository.hasTimeConflict(reserveInDto.getSeatId(), startDateTime, endDateTime)) {
			throw new BusinessException("Seat already reserved during this time.");
		}
		
		var requested = Duration.between(startDateTime, endDateTime);
		long requestedMinutes = requested.toMinutes();
		
		long totalMinutes = reservationRepository.getUserReservedMinutesForDay(userId, date).orElse(Long.valueOf(0)); 
		
		System.out.println(requestedMinutes);
		System.out.println(totalMinutes);
		
		if(totalMinutes + requestedMinutes > 8 * 60) {
			throw new BusinessException("Exceeds daily reservation time (8 hours).");
		}

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
		
		System.out.println(userId);

		Reservation reservation = reservationRepository.findById(reservationId);
		if (reservation == null) {
			throw new BusinessException("Not found reservation");
		}

		if (!reservation.getStatus().equals("RESERVED")) {
			throw new BusinessException("Reservation status is not RESERVED");
		}


		if (reservation.getEmployeeId() != userId) {
			throw new BusinessException("Employee ID does not match user ID");
		}

		reservationRepository.cancel(reservationId, userId);
	}

	public void extend(ExtendReservationInDto extendReservationInDto, Integer userId) {
		Reservation lastReservation = reservationRepository.findById(extendReservationInDto.getReservationId());
		if (lastReservation == null) {
			throw new BusinessException("Reservation not found");
		}

		if (!"IN_USE".equals(lastReservation.getStatus())) {
			throw new BusinessException("Reservation must be in IN_USE status");
		}

		Seat seat = seatRepository.findById(lastReservation.getSeatId());
		if (seat == null) {
			throw new BusinessException("Seat not found");
		}

		if (!"UNAVAILABLE".equals(seat.getStatus())) {
			throw new BusinessException("Seat must be in UNAVAILABLE status");
		}

		Reservation newReservation = new Reservation();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		LocalDateTime endLocalDateTime = LocalDateTime.parse(extendReservationInDto.getEndTime(), formatter);

		if (endLocalDateTime.getHour() >= 0 && endLocalDateTime.getHour() < 1) {
			throw new BusinessException("The end time of an extended reservation must be before 00:00 of the next day");
		}

		if (reservationRepository.hasTimeConflict(lastReservation.getSeatId(), lastReservation.getEndTime().plusMinutes(1), endLocalDateTime)) {
			throw new BusinessException("Time conflict detected");
		}

		Long totalMinutes = reservationRepository.getUserReservedMinutesForDay(userId, LocalDate.now())
				.orElse(Long.valueOf(0));
		if (totalMinutes + 60 > 480) {
			throw new BusinessException("Total reservation time for the day must not exceed 8 hours");
		}
		


		newReservation.setCheckInAt(endLocalDateTime.minusHours(1));
		newReservation.setCreatedAt(LocalDateTime.now());
		newReservation.setEmployeeId(userId);
		newReservation.setEndTime(endLocalDateTime);
		newReservation.setExtendedFromReservationId(extendReservationInDto.getReservationId());
		newReservation.setSeatId(lastReservation.getSeatId());
		newReservation.setStartTime(lastReservation.getEndTime());
		newReservation.setStatus("IN_USE");

		reservationRepository.extend(newReservation);
	}

}
