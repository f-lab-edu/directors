package com.directors.presentation.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;
import com.directors.domain.chat.LiveChatManager;
import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.exception.NoSuchUserException;
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
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
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

        liveChatManager.sendMessage(request.roomId(), request.chatContent(), userByToken);

        saveChat(request.roomId(), request.chatContent(), userByToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/chatList/{roomId}/{offset}/{size}")
    public ResponseEntity<List<ChatListResponse>> chatList(
            @PathVariable Long roomId, @PathVariable Integer offset, @PathVariable Integer size,
            @AuthenticationPrincipal String userByToken
    ) {
        roomValidate(roomId, userByToken);

        List<Chat> chatList = chatRepository.findChatListByRoomId(roomId, offset, size);
        List<ChatListResponse> responseList = chatList.stream().map(chat -> ChatListResponse.from(chat)).collect(Collectors.toList());

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    private void roomValidate(Long roomId, String userId) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        room.validateRoomUser(userId);
    }

    private void saveChat(Long roomId, String chatContent, String sendUserId) {
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId, sendUserId));
        User user = userRepository
                .findById(sendUserId)
                .orElseThrow(() -> new NoSuchUserException(sendUserId));

        Chat chat = Chat.of(room, chatContent, user);

        chatRepository.save(chat);
    }
}