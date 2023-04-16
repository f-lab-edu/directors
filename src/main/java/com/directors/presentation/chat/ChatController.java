package com.directors.presentation.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;
import com.directors.domain.chat.LiveChatManager;
import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import com.directors.presentation.chat.request.SendChatRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final LiveChatManager liveChatManager;

    @GetMapping("/receiveStream/{roomId}")
    public SseEmitter getReceiveStream(@PathVariable Long roomId, @AuthenticationPrincipal String userByToken) {
        roomValidate(roomId, userByToken);

        SseEmitter receiveStream = new SseEmitter();

        liveChatManager.addReceiver(roomId, receiveStream);

        receiveStream.onCompletion(() -> liveChatManager.removeReceiver(roomId, receiveStream));
        receiveStream.onTimeout(() -> liveChatManager.removeReceiver(roomId, receiveStream));
        receiveStream.onError(e -> liveChatManager.removeReceiver(roomId, receiveStream));

        return receiveStream;
    }

    @PostMapping("/send")
    public ResponseEntity<HttpStatus> send(@Valid @RequestBody SendChatRequest request, @AuthenticationPrincipal String userByToken) {
        roomValidate(request.roomId(), userByToken);

        saveChat(request.roomId(), request.chatContent(), userByToken);

        liveChatManager.sendMessage(request.roomId(), request.chatContent(), userByToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void roomValidate(Long roomId, String userByToken) {
        Room room = roomRepository.findByRoomId(roomId).orElseThrow();
        room.validateRoomUser(userByToken);
    }

    private void saveChat(Long roomId, String chatContent, String sendUserId) {
        Chat chat = Chat.of(roomId, chatContent, sendUserId, LocalDateTime.now());
        chatRepository.save(chat);
    }
}