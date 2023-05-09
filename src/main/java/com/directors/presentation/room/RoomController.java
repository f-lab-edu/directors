package com.directors.presentation.room;

import com.directors.application.room.RoomService;
import com.directors.presentation.room.response.GetRoomInfosByDirectorIdResponse;
import com.directors.presentation.room.response.GetRoomInfosByQuestionerIdResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create/{questionId}")
    public ResponseEntity<Long> create(
            @Valid @RequestBody CreateRoomRequest request, @AuthenticationPrincipal String userIdByToken
    ) {
        return new ResponseEntity<>(roomService.create(request, userIdByToken), HttpStatus.OK);
    }

    @GetMapping("/roomInfosBydirectorId/{directorId}")
    public ResponseEntity<List<GetRoomInfosByDirectorIdResponse>> getRoomInfosBydirectorId(
            @PathVariable @NotBlank(message = "입력 값이 존재하지않습니다.") String directorId
    ) {
        var responseList = roomService.getRoomInfosByDirectorId(directorId);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/roomInfosByQuestionerId/{questionerId}")
    public ResponseEntity<List<GetRoomInfosByQuestionerIdResponse>> getRoomInfosByQuestionerId(
            @PathVariable @NotBlank(message = "입력 값이 존재하지않습니다.") String questionerId
    ) {
        var responseList = roomService.getRoomInfosByQuestionerId(questionerId);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
