package com.directors.presentation.region;

import com.directors.application.region.RegionService;
import com.directors.presentation.region.request.AuthenticateRegionRequest;
import com.directors.presentation.region.response.AuthenticateRegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/region")
public class RegionController {
    private final RegionService regionService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateRegionResponse> authenticate(@RequestBody AuthenticateRegionRequest request, @AuthenticationPrincipal String userIdByToken) {
        AuthenticateRegionResponse response = regionService.authenticate(request, userIdByToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
