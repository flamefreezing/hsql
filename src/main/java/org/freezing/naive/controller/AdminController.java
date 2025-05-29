package org.freezing.naive.controller;

import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.ErrorResponse;
import org.freezing.naive.dto.ForceReturnInDto;
import org.freezing.naive.dto.ReturnSeatInDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AdminController {
	private final AdminService adminService;
	
	@PostMapping("/admin/return")
    public ResponseEntity<?> forceReturn(@RequestBody ForceReturnInDto forceReturnInDto) throws Exception {
        try {
        	adminService.forceReturn(forceReturnInDto.getSeatId());
            return new ResponseEntity<>(new DataResponse("Successfully Force Returned"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
