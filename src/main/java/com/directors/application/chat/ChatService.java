package com.directors.application.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;
import com.directors.domain.chat.LiveChatManager;
import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.user.UserRepository;
import com.directors.presentation.chat.request.ChatListRequest;
import com.directors.presentation.chat.request.SendChatRequest;
import com.directors.presentation.chat.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final LiveChatManager liveChatManager;

    public SseEmitter getChatStream(Long roomId, String userId) {
        roomValidate(roomId, userId);

        SseEmitter receiveStream = new SseEmitter();

        liveChatManager.addReceiver(roomId, receiveStream);

        receiveStream.onCompletion(() -> liveChatManager.removeReceiver(roomId, receiveStream));
        receiveStream.onTimeout(() -> liveChatManager.removeReceiver(roomId, receiveStream));
        receiveStream.onError(e -> liveChatManager.removeReceiver(roomId, receiveStream));

        return receiveStream;
    }

    public boolean sendChat(SendChatRequest request, String userId) {
        roomValidate(request.roomId(), userId);

        liveChatManager.sendChat(request.roomId(), request.chatContent(), request.sendTime(), userId);

        chatRepository.save(Chat.of(request.roomId(), request.chatContent(), userId, request.sendTime()));

        return true;
    }

    public List<ChatResponse> chatList(ChatListRequest request) {
        roomValidate(request.roomId(), request.userId());

        List<Chat> chatList = chatRepository.findChatListByRoomId(request.roomId(), request.offset(), request.size());

        return chatList.stream()
                .map(chat -> ChatResponse.from(request.roomId(), chat))
                .collect(Collectors.toList());
    }

    private void roomValidate(Long roomId, String userId) {
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId, userId));
        room.validateRoomUser(userId);
    }
}
