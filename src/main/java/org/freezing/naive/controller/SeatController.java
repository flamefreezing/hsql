package org.freezing.naive.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.freezing.naive.dto.*;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/seats/check-in")
    public ResponseEntity<?> checkin(@RequestBody CheckinSeatInDto checkinSeatInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            seatService.checkin(checkinSeatInDto.getReservationId(), userId);
            return new ResponseEntity<>(new DataResponse("Successfully Checked-in"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @PostMapping("/seats/return")
    public ResponseEntity<?> returnSeat(@RequestBody ReturnSeatInDto returnSeatInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            seatService.returnSeat(returnSeatInDto.getReservationId(), userId);
            return new ResponseEntity<>(new DataResponse("Successfully Returned"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
