package org.freezing.naive.controller;

import lombok.RequiredArgsConstructor;
import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.LoginRequest;
import org.freezing.naive.dto.MessageResponse;
import org.freezing.naive.dto.RegisterRequest;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            authenticationService.register(registerRequest);
            return new ResponseEntity<>(new MessageResponse("User registered successfully"), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatusCode.valueOf(e.getCode()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> register(@RequestBody LoginRequest loginRequest) {
        try {
            String jwt = authenticationService.login(loginRequest);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatusCode.valueOf(e.getCode()));
        }
    }
}
