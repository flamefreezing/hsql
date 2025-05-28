package org.freezing.naive.controller;

import java.time.LocalDate;
import java.util.List;

import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.GetAvailableSetsOutDto;
import org.freezing.naive.dto.GetSeatsByFloorIdOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.SeatService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getSeatsByFloorId(@RequestParam("floorId") Integer floorId) {
        List<GetSeatsByFloorIdOutDto> seats = seatService.getSeatsByFloorId(floorId);
        return new ResponseEntity<>(new DataResponse(seats), HttpStatus.OK);
    }
    
    @GetMapping("/seats/available")
    public ResponseEntity<?> getAvailableSets(@RequestParam("date") LocalDate date,@RequestParam("startTime")  String startTime,
    		@RequestParam("endTime") String endTime, @RequestParam("skip") Integer skip,@RequestParam("limit") Integer limit) {
        try {
            List<GetAvailableSetsOutDto> availableSeats = seatService.getAvailableSets(date, startTime, endTime, skip, limit);
            return new ResponseEntity<>(new DataResponse(availableSeats), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
