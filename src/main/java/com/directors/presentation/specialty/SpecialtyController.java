package com.directors.presentation.specialty;


import com.directors.application.specialty.SpecialtyService;
import com.directors.presentation.specialty.request.CreateSpecialtyRequest;
import com.directors.presentation.specialty.request.UpdateSpecialtyRequest;
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
    public ResponseEntity<HttpStatus> create(@RequestBody CreateSpecialtyRequest createSpecialtyRequest, @AuthenticationPrincipal String userIdByToken) {
        specialtyService.createSpecialty(createSpecialtyRequest.toEntity(), userIdByToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<HttpStatus> update(@RequestBody UpdateSpecialtyRequest updateSpecialtyRequest) {
        specialtyService.updateSpecialty(updateSpecialtyRequest.toEntity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@PathVariable String specialtyId) {
        specialtyService.deleteSpecialty(specialtyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
