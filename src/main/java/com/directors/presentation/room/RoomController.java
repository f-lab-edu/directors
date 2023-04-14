package com.directors.presentation.room;

import com.directors.application.room.RoomService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create/{questionId}")
    public ResponseEntity<Long> create(
            @PathVariable @NotBlank(message = "입력 값이 존재하지않습니다.") Long questionId,
            @AuthenticationPrincipal String userIdByToken
    ) {
        return new ResponseEntity<>(roomService.create(questionId, userIdByToken), HttpStatus.OK);
    }
}
