package org.freezing.naive.controller;

import java.util.List;

import org.freezing.naive.dto.CheckinSeatInDto;
import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.ErrorResponse;
import org.freezing.naive.dto.GetAvailableSetsOutDto;
import org.freezing.naive.dto.GetSeatsByFloorIdOutDto;
import org.freezing.naive.dto.ReturnSeatInDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seats")
    public ResponseEntity<?> getSeatsByFloorId(@RequestParam(required = false, name = "floorId") Integer floorId) {
        List<GetSeatsByFloorIdOutDto> seats = seatService.getSeatsByFloorId(floorId);
        return new ResponseEntity<>(new DataResponse(seats), HttpStatus.OK);
    }
    
    @GetMapping("/seats/available")
    public ResponseEntity<?> getAvailableSets(
    		@RequestParam(required = false, name = "date") String date,
    		@RequestParam(required = false, name = "startTime")  String startTime,
    		@RequestParam(required = false, name = "endTime") String endTime, 
    		@RequestParam(required = false, name = "skip") Integer skip,
    		@RequestParam(required = false, name = "limit") Integer limit) {
        try {
            List<GetAvailableSetsOutDto> availableSeats = seatService.getAvailableSets(date, startTime, endTime, skip, limit);
            return new ResponseEntity<>(new DataResponse(availableSeats), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @PostMapping("/seats/check-in")
    public ResponseEntity<?> checkin(@RequestBody CheckinSeatInDto checkinSeatInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            seatService.checkin(checkinSeatInDto.getReservationId(), userId);
            return new ResponseEntity<>(new DataResponse("Successfully Checked-in"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @PostMapping("/seats/return")
    public ResponseEntity<?> returnSeat(@RequestBody ReturnSeatInDto returnSeatInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            seatService.returnSeat(returnSeatInDto.getReservationId(), userId);
            return new ResponseEntity<>(new DataResponse("Successfully Returned"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
