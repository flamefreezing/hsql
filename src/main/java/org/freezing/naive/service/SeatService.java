package org.freezing.naive.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.freezing.naive.dto.GetAvailableSeatsOutDto;
import org.freezing.naive.dto.SeatOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.SeatRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public List<SeatOutDto> getSeatsByFloorId(Integer floorId) {

		if (floorId == null) {
			throw new BusinessException("Floor ID is required");
		}

		return seatRepository.getSeatsByFloorId(floorId);
	}

	public List<GetAvailableSeatsOutDto> getAvailableSeats(String date, String startTime, String endTime, Integer skip,
			Integer limit) {

		if (date == null || startTime == null || endTime == null) {
			throw new BusinessException("date, startTime, and endTime are required");
		}

		if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
			throw new BusinessException("date must be in format yyyy-MM-dd");
		}

		if (!startTime.matches("\\d{2}:\\d{2}") || !endTime.matches("\\d{2}:\\d{2}")) {
			throw new BusinessException("startTime and endTime must be in format hh:mm");
		}

		LocalDate localDate = LocalDate.parse(date);
		LocalTime startLocalTime = LocalTime.parse(startTime);
		LocalTime endLocalTime = LocalTime.parse(endTime);

		LocalDateTime startDateTime = LocalDateTime.of(localDate, startLocalTime);
		LocalDateTime endDateTime = LocalDateTime.of(localDate, endLocalTime);

		if (skip == null) {
			skip = 0;
		}
		if (limit == null) {
			limit = 5;
		}

		List<GetAvailableSeatsOutDto> availableSeats = seatRepository.getAvailableSeats(startDateTime, endDateTime,
				skip, limit);
		

		return availableSeats;
	}
   

}
