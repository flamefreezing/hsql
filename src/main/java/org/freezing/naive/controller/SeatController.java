package org.freezing.naive.controller;

import java.util.List;

import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.GetAvailableSeatsOutDto;
import org.freezing.naive.dto.MessageResponse;
import org.freezing.naive.dto.SeatOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seats")
    public ResponseEntity<?> getSeatsByFloorId(@RequestParam(required = false, name = "floorId") Integer floorId) {
        try {
        	List<SeatOutDto> seats = seatService.getSeatsByFloorId(floorId);
        	return new ResponseEntity<>(new DataResponse(seats), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatusCode.valueOf(e.getCode()));
		}
    }
    
    @GetMapping("/seats/available")
    public ResponseEntity<?> getAvailableSeats(
    		@RequestParam(name = "date", required = false) String date,
    		@RequestParam(name = "startTime", required = false) String startTime,
            @RequestParam(name = "endTime", required = false) String endTime,
            @RequestParam(name = "skip", required = false) Integer skip,
            @RequestParam(name = "limit", required = false) Integer limit
    		) {
        try {
            List<GetAvailableSeatsOutDto> availableSeats = seatService.getAvailableSeats(date, startTime, endTime, skip, limit);
            return new ResponseEntity<>(new DataResponse(availableSeats), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
