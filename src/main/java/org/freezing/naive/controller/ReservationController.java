package org.freezing.naive.controller;

import java.time.LocalDate;
import java.util.List;

import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.GetReservationHistoryOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
