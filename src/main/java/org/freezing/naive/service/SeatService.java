package org.freezing.naive.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.freezing.naive.dto.GetAvailableSetsOutDto;
import org.freezing.naive.dto.GetSeatsByFloorIdOutDto;
import org.freezing.naive.dto.Reservation;
import org.freezing.naive.dto.Seat;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.ReservationRepository;
import org.freezing.naive.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
	private final ReservationRepository reservationRepository;

    public List<GetSeatsByFloorIdOutDto> getSeatsByFloorId(Integer floorId) {

		if (floorId == null) {
			throw new BusinessException("floorId is required");
		}

		return seatRepository.getSeatsByFloorId(floorId);
	}
    
    public List<GetAvailableSetsOutDto> getAvailableSets(String date, String startTime, String endTime, Integer skip,
			Integer limit) {

		if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
			throw new BusinessException("You must validate whether the input data format is correct");
		}

		if (startTime == null || !startTime.matches("\\d{2}:\\d{2}")) {
			throw new BusinessException("You must validate whether the input data format is correct");
		}

		if (endTime == null || !endTime.matches("\\d{2}:\\d{2}")) {
			throw new BusinessException("You must validate whether the input data format is correct");
		}

		LocalTime startLocalTime = LocalTime.parse(startTime);
		LocalTime endLocalTime = LocalTime.parse(endTime);
		LocalDateTime startDateTime = LocalDateTime.of(LocalDate.parse(date), startLocalTime);

		if (startDateTime.isBefore(LocalDateTime.now())) {
			throw new BusinessException(
					"The start time for searching reservations must not be earlier than the current time.");
		}

		LocalDateTime endDateTime = LocalDateTime.of(LocalDate.parse(date), endLocalTime);

		if (skip == null) {
			skip = 0;
		}
		if (limit == null) {
			limit = 5;
		}

		List<GetAvailableSetsOutDto> availableSeats = seatRepository.getAvailableSeats(startDateTime, endDateTime, skip,
				limit);
		if (availableSeats.isEmpty()) {
			throw new BusinessException("No available seats found", 404);
		}
		return availableSeats;
	}

	@Transactional
	public void checkin(Integer reservationId, Integer userId) {
		Reservation reservation = reservationRepository.findById(reservationId);
		if (reservation == null) {
			throw new BusinessException("Reservation not found");
		}

		if (!reservation.getStatus().equals("RESERVED")) {
			throw new BusinessException("Reservation status must be RESERVED");
		}

		Integer seatId = reservation.getSeatId();

		Seat seat = seatRepository.findById(seatId);
		if (seat == null) {
			throw new BusinessException("Seat not found");
		}

		if (!seat.getStatus().equals("AVAILABLE")) {
			throw new BusinessException("Seat is not available");
		}

		if (!reservation.getEmployeeId().equals(userId)) {
			throw new BusinessException("Employee ID must match User ID");
		}

		reservationRepository.setStatus("IN_USE", reservationId);
		seatRepository.setStatus("UNAVAILABLE", seatId);
	}

	@Transactional
	public void returnSeat(Integer reservationId, Integer userId) {
		Reservation reservation = reservationRepository.findById(reservationId);

		if (reservation == null) {
			throw new BusinessException("Reservation not found");
		}

		if (!reservation.getStatus().equals("IN_USE")) {
			throw new BusinessException("Reservation is not in use");
		}

		Integer seatId = reservation.getSeatId();

		Seat seat = seatRepository.findById(seatId);

		if (seat == null) {
			throw new BusinessException("Seat not found");
		}

		if (!seat.getStatus().equals("UNAVAILABLE")) {
			throw new BusinessException("Seat is not in the UNAVAILABLE status");
		}

		if (!reservation.getEmployeeId().equals(userId)) {
			throw new BusinessException("Employee ID does not match user ID");
		}
		

		reservationRepository.setStatus("COMPLETED", reservationId);
		seatRepository.setStatus("AVAILABLE", seatId);
		
		Integer extendedFromReservationId = reservation.getExtendedFromReservationId();
		while(extendedFromReservationId != null) {
			Reservation prevReservation = reservationRepository.findById(extendedFromReservationId);
			if(prevReservation == null) break;
			reservationRepository.setStatus("COMPLETED", prevReservation.getId());
			extendedFromReservationId = prevReservation.getExtendedFromReservationId();
		}
		
		extendedFromReservationId = reservation.getId();
		var nextReservation = reservationRepository.findByExtendedFromReservationId(reservation.getId());
		while(nextReservation != null) {
			reservationRepository.setStatus("COMPLETED", nextReservation.getId());
			nextReservation = reservationRepository.findByExtendedFromReservationId(nextReservation.getId());
		}
		
	}

}
