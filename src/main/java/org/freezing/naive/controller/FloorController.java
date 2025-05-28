package org.freezing.naive.controller;

import java.util.List;

import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.GetFloorsOutDto;
import org.freezing.naive.exception.BusinessException;
import org.freezing.naive.service.FloorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class FloorController {

    private final FloorService floorService;

    @GetMapping("/seats/floors")
    public ResponseEntity<?> getFloors(@RequestParam("buildingId") Integer buildingId) {
        try {
            List<GetFloorsOutDto> floors = floorService.getFloors(buildingId);
            return new ResponseEntity<>(new DataResponse(floors), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<>(new DataResponse(e.getMessage()), HttpStatus.valueOf(e.getCode()));
        }
    }
}
