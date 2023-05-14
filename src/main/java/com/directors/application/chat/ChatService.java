package com.directors.application.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;
import com.directors.domain.chat.LiveChatManager;
import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.presentation.chat.request.ChatListRequest;
import com.directors.presentation.chat.request.SendChatRequest;
import com.directors.presentation.chat.response.ChatListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;
    private final LiveChatManager liveChatManager;

    public SseEmitter getReceiveStream(Long roomId, String userId) {
        roomValidate(roomId, userId);

        SseEmitter receiveStream = new SseEmitter();

        liveChatManager.addReceiver(roomId, receiveStream);

        receiveStream.onCompletion(() -> liveChatManager.removeReceiver(roomId, receiveStream));
        receiveStream.onTimeout(() -> liveChatManager.removeReceiver(roomId, receiveStream));
        receiveStream.onError(e -> liveChatManager.removeReceiver(roomId, receiveStream));

        return receiveStream;
    }

    public boolean send(SendChatRequest request, String userId) {
        roomValidate(request.roomId(), userId);

        liveChatManager.sendMessage(request.roomId(), request.chatContent(), userId);

        saveChat(request.roomId(), request.chatContent(), userId);

        return true;
    }

    public List<ChatListResponse> chatList(ChatListRequest request) {
        roomValidate(request.roomId(), request.userId());

        List<Chat> chatList = chatRepository.findChatListByRoomId(request.roomId(), request.offset(), request.size());

        return chatList.stream()
                .map(chat -> ChatListResponse.from(chat))
                .collect(Collectors.toList());
    }

    private void saveChat(Long roomId, String chatContent, String sendUserId) {
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId, sendUserId));
        User user = userRepository
                .findById(sendUserId)
                .orElseThrow(() -> new NoSuchUserException(sendUserId));

        chatRepository.save(Chat.of(room, chatContent, user));
    }

    private void roomValidate(Long roomId, String userId) {
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId, userId));
        room.validateRoomUser(userId);
    }
}
