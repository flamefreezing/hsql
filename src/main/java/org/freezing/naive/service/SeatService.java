package org.freezing.naive.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.freezing.naive.dto.GetAvailableSetsOutDto;
import org.freezing.naive.dto.GetSeatsByFloorIdOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.repository.SeatRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public List<GetSeatsByFloorIdOutDto> getSeatsByFloorId(Integer floorId) {
        return seatRepository.getSeatsByFloorId(floorId);
    }
    
    public List<GetAvailableSetsOutDto> getAvailableSets(LocalDate date, String startTime, String endTime, Integer skip,
			Integer offset) {
		LocalTime startLocalTime = LocalTime.parse(startTime);
		LocalTime endLocalTime = LocalTime.parse(endTime);
		LocalDateTime startDateTime = LocalDateTime.of(date, startLocalTime);
		LocalDateTime endDateTime = LocalDateTime.of(date, endLocalTime);

		List<GetAvailableSetsOutDto> availableSeats = seatRepository.getAvailableSeats(startDateTime, endDateTime, skip, offset);
		if (availableSeats.isEmpty()) {
			throw new BusinessException("No available seats found", 404);
		}
		return availableSeats;
	}

}
