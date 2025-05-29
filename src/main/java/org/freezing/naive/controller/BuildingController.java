package org.freezing.naive.controller;

import java.util.List;

import org.freezing.naive.dto.DataResponse;
import org.freezing.naive.dto.GetBuildingsOutDto;
import org.freezing.naive.service.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping("/seats/buildings")
    public ResponseEntity<?> getBuildings() {
        List<GetBuildingsOutDto> buildings = buildingService.getBuildings();
        return new ResponseEntity<>(new DataResponse(buildings), HttpStatus.OK);
    }
}
