package com.directors.presentation.specialty;


import com.directors.application.specialty.SpecialtyService;
import com.directors.presentation.specialty.request.CreateSpecialtyRequest;
import com.directors.presentation.specialty.request.UpdateSpecialtyRequest;
import com.directors.presentation.specialty.response.CreateSpecialtyResponse;
import com.directors.presentation.specialty.response.UpdateSpecialtyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/specialty")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @PostMapping("/create")
    public ResponseEntity<CreateSpecialtyResponse> create(@RequestBody CreateSpecialtyRequest request, @AuthenticationPrincipal String userIdByToken) {
        return new ResponseEntity<>(specialtyService.createSpecialty(request, userIdByToken), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateSpecialtyResponse> update(@RequestBody UpdateSpecialtyRequest request) {
        return new ResponseEntity<>(specialtyService.updateSpecialty(request), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{specialtyId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long specialtyId) {
        specialtyService.deleteSpecialty(specialtyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
