package com.directors.presentation.chat;

import com.directors.application.chat.ChatService;
import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;
import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.user.UserRepository;
import com.directors.presentation.chat.request.ChatListRequest;
import com.directors.presentation.chat.request.SendChatRequest;
import com.directors.presentation.chat.response.ChatListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/receiveStream/{roomId}")
    public SseEmitter getReceiveStream(@PathVariable Long roomId, @AuthenticationPrincipal String userIdByToken) {
        return chatService.getReceiveStream(roomId, userIdByToken);
    }

    @PostMapping("/send")
    public ResponseEntity<Boolean> send(@Valid @RequestBody SendChatRequest request, @AuthenticationPrincipal String userIdByToken) {
        return new ResponseEntity<>(chatService.send(request, userIdByToken), HttpStatus.OK);
    }

    @GetMapping("/chatList")
    public ResponseEntity<List<ChatListResponse>> chatList(@Valid @RequestBody ChatListRequest request) {
        return new ResponseEntity<>(chatService.chatList(request), HttpStatus.OK);
    }
}