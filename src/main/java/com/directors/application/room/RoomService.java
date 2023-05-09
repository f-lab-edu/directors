package com.directors.application.room;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;
import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.presentation.room.CreateRoomRequest;
import com.directors.presentation.room.response.GetRoomInfosByDirectorIdResponse;
import com.directors.presentation.room.response.GetRoomInfosByQuestionerIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final QuestionRepository questionRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public Long create(CreateRoomRequest request, String directorId) {
        var question = getQuestionById(request.questionId());

        question.canCreateChatRoom(directorId, request.requestTime());

        var room = Room.of(question, question.getDirector(), question.getQuestioner());
        Room savedRoom = roomRepository.save(room);

        question.changeQuestionStatusToChat();

        return savedRoom.getId();
    }

    @Transactional
    public List<GetRoomInfosByDirectorIdResponse> getRoomInfosByDirectorId(String directorId) {
        List<GetRoomInfosByDirectorIdResponse> responseList = new ArrayList<>();

        List<Room> roomByDirectorId = roomRepository.findByDirectorId(directorId);

        for (Room room : roomByDirectorId) {
            Chat recentChat = getRecentChatByRoom(room);

            var response = new GetRoomInfosByDirectorIdResponse(
                    room.getId(), room.getQuestion().getId(), room.getDirector().getId(), recentChat.getContent(),
                    recentChat.getCreatedTime());

            responseList.add(response);
        }

        return responseList;
    }

    @Transactional
    public List<GetRoomInfosByQuestionerIdResponse> getRoomInfosByQuestionerId(String questionerId) {
        List<GetRoomInfosByQuestionerIdResponse> responseList = new ArrayList<>();

        List<Room> roomByQuestionerId = roomRepository.findByQuestionerId(questionerId);

        for (Room room : roomByQuestionerId) {
            Chat recentChat = getRecentChatByRoom(room);

            var response = new GetRoomInfosByQuestionerIdResponse(
                    room.getId(), room.getQuestion().getId(), room.getQuestioner().getId(), recentChat.getContent(),
                    recentChat.getCreatedTime());

            responseList.add(response);
        }

        return responseList;
    }

    private Question getQuestionById(Long questionId) {
        return questionRepository
                .findById(questionId)
                .orElseThrow(QuestionNotFoundException::new);
    }

    private Chat getRecentChatByRoom(Room room) {
        List<Chat> chatListByRoomId = chatRepository.findChatListByRoomId(room.getId(), 0, 1);

        if (chatListByRoomId.size() == 0) {
            return Chat.of(room, "최근 채팅 내역이 존재하지 않습니다.", null);
        }

        return chatListByRoomId.get(0);
    }

}
