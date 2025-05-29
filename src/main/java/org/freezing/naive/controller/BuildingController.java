package org.freezing.naive.controller;

import lombok.RequiredArgsConstructor;
import org.freezing.naive.dto.GetBuildingsOutDto;
import org.freezing.naive.service.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BuildingController {

    private final BuildingService buildingService;

    @GetMapping("/seats/buildings")
    public ResponseEntity<?> getBuildings() {
        List<GetBuildingsOutDto> buildings = buildingService.getBuildings();
        return new ResponseEntity<>(Map.of("data", buildings), HttpStatus.OK);
    }
}
