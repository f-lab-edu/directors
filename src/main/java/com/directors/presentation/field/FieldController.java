package com.directors.presentation.field;


import com.directors.application.field.FieldService;
import com.directors.presentation.field.request.CreateFieldRequest;
import com.directors.presentation.user.request.UpdateFieldRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/field")
public class FieldController {

    private final FieldService fieldService;

    @PostMapping("/createField")
    public ResponseEntity<HttpStatus> createField(@RequestBody CreateFieldRequest createFieldRequest, @AuthenticationPrincipal String userIdByToken) {
        fieldService.createField(createFieldRequest.toEntity(), userIdByToken);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/updateField")
    public ResponseEntity<HttpStatus> updateField(@RequestBody UpdateFieldRequest updateFieldRequest) {
        fieldService.updateField(updateFieldRequest.toEntity());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/deleteField")
    public ResponseEntity<HttpStatus> deleteField(@PathVariable String fieldId) {
        fieldService.deleteField(fieldId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
