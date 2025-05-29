package org.freezing.naive.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.freezing.naive.dto.Reservation;
import org.freezing.naive.repository.ReservationRepository;
import org.freezing.naive.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final SeatRepository seatRepository;
	private final ReservationRepository reservationRepository;

	@Transactional
	public void forceReturn(Integer seatId) throws Exception {
		var reservation = reservationRepository.findInTime(seatId);
		if (reservation == null) {
			throw new Exception("Reservation not found for seatId: " + seatId);
		}

		if (!reservation.getStatus().equals("IN_USE")) {
			throw new Exception("Reservation is not in IN_USE status for seatId: " + seatId);
		}

		seatRepository.setStatus("AVAILABLE", seatId);

		reservationRepository.setStatus("FORCED_CANCEL", reservation.getId());
		Integer extendedFromReservationId = reservation.getExtendedFromReservationId();
		while (extendedFromReservationId != null) {
			Reservation prevReservation = reservationRepository.findById(extendedFromReservationId);
			if (prevReservation == null)
				break;
			reservationRepository.setStatus("FORCED_CANCEL", prevReservation.getId());
			extendedFromReservationId = prevReservation.getExtendedFromReservationId();
		}

		extendedFromReservationId = reservation.getId();
		var nextReservation = reservationRepository.findByExtendedFromReservationId(reservation.getId());
		while (nextReservation != null) {
			reservationRepository.setStatus("FORCED_CANCEL", nextReservation.getId());
			nextReservation = reservationRepository.findByExtendedFromReservationId(nextReservation.getId());
		}
	}

}
