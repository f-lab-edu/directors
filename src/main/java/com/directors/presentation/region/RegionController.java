package com.directors.presentation.region;

import com.directors.application.region.RegionService;
import com.directors.presentation.region.response.NearestAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/region")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/nearestAddress/{distance}")
    public ResponseEntity<List<NearestAddressResponse>> getNearestAddress(@PathVariable int distance, @AuthenticationPrincipal String userIdByToken) {
        return new ResponseEntity<>(regionService.getNearestAddress(distance, userIdByToken), HttpStatus.OK);
    }
}
