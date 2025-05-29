package org.freezing.naive.controller;

import java.util.List;

import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.GetOwnReservationsOutDto;
import org.freezing.naive.dto.GetReservationHistoryOutDto;
import org.freezing.naive.dto.MessageResponse;
import org.freezing.naive.dto.ReserveInDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<?> getOwnReservations(HttpServletRequest request) {
        try {
        	var employeeId = Integer.parseInt((String) request.getAttribute("employeeId"));
        	List<GetOwnReservationsOutDto> reservations = reservationService.getOwnReservations(employeeId);
            return new ResponseEntity<>(new DataResponse(reservations), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatusCode.valueOf(e.getCode()));
		}
    }
    
    @PostMapping
    public ResponseEntity<?> register(@RequestBody ReserveInDto reserveInDto, HttpServletRequest request) {
        try {
        	var employeeId = Integer.parseInt((String) request.getAttribute("employeeId"));
        	reservationService.reserve(reserveInDto, employeeId);
            return new ResponseEntity<>(new DataResponse("Successfully Reserved"), HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatusCode.valueOf(e.getCode()));
		}
    }
    
    @GetMapping("/history")
    public ResponseEntity<?> getReservationHistory(
    		@RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "skip", required = false) Integer skip,
            @RequestParam(name = "limit", required = false) Integer limit
           ) {
        try {
            List<GetReservationHistoryOutDto> history = reservationService.getReservationHistory(startDate, endDate, skip, limit);
            return new ResponseEntity<>(new DataResponse(history), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
