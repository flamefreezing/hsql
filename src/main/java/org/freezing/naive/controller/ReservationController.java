package org.freezing.naive.controller;

import java.util.List;

import org.freezing.naive.dto.CancelReservationInDto;
import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.ErrorResponse;
import org.freezing.naive.dto.ExtendReservationInDto;
import org.freezing.naive.dto.GetOwnReservationsOutDto;
import org.freezing.naive.dto.GetReservationHistoryOutDto;
import org.freezing.naive.dto.ReserveInDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.ReservationService;
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
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations/history")
    public ResponseEntity<?> getReservationHistory(@RequestParam(required = false, name = "startDate") String startDate,
    		@RequestParam(required = false, name = "endDate") String endDate, 
    		@RequestParam(required = false, name = "skip") Integer skip, 
    		@RequestParam(required = false, name = "limit") Integer limit) {
        try {
            List<GetReservationHistoryOutDto> reservationHistory = reservationService.getReservationHistory(startDate, endDate, skip, limit);
            return new ResponseEntity<>(new DataResponse(reservationHistory), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getOwnReservations(HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            List<GetOwnReservationsOutDto> reservations = reservationService.getOwnReservations(userId);
            return new ResponseEntity<>(new DataResponse(reservations), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> reserve(@RequestBody ReserveInDto reserveInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            reservationService.reserve(reserveInDto, userId);
            return new ResponseEntity<>(new DataResponse("Successfully Reserved"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @PostMapping("/reservations/cancel")
    public ResponseEntity<?> cancel(@RequestBody CancelReservationInDto cancelReservationInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            reservationService.cancel(cancelReservationInDto.getReservationId(), userId);
            return new ResponseEntity<>(new DataResponse("Successfully Canceled"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
    
    @PostMapping("/reservations/extend")
    public ResponseEntity<?> extend(@RequestBody ExtendReservationInDto extendReservationInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            reservationService.extend(extendReservationInDto, userId);
            return new ResponseEntity<>(new DataResponse("Successfully Extended"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
