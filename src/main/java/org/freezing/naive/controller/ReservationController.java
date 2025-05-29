package org.freezing.naive.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.freezing.naive.dto.*;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/reservations/history")
    public ResponseEntity<?> getReservationHistory(@RequestParam("startDate") String startDate,
    		@RequestParam("endDate") String endDate, @RequestParam("skip") Integer skip,@RequestParam("limit") Integer limit) {
        try {
            List<GetReservationHistoryOutDto> reservationHistory = reservationService.getReservationHistory(startDate, endDate, skip, limit);
            return new ResponseEntity<>(new DataResponse(reservationHistory), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getOwnReservations(HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            List<GetOwnReservationsOutDto> reservations = reservationService.getOwnReservations(userId);
            return new ResponseEntity<>(new DataResponse(reservations), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> reserve(@RequestBody ReserveInDto reserveInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            reservationService.reserve(reserveInDto, userId);
            return new ResponseEntity<>(new DataResponse("Successfully Reserved"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }

    @PostMapping("/reservations/cancel")
    public ResponseEntity<?> cancel(@RequestBody CancelReservationInDto cancelReservationInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            reservationService.cancel(cancelReservationInDto.getReservationId(), userId);
            return new ResponseEntity<>(new DataResponse("Successfully Canceled"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
    
    @PostMapping("/reservations/extend")
    public ResponseEntity<?> cancel(@RequestBody ExtendReservationInDto extendReservationInDto, HttpServletRequest request) {
        try {
            Integer userId = Integer.parseInt((String)request.getAttribute("userId"));
            reservationService.extend(extendReservationInDto, userId);
            return new ResponseEntity<>(new DataResponse("Successfully Extended"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
