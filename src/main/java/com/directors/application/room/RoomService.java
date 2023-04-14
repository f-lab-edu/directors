package com.directors.application.room;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Long create(Long questionId, String directorId) {
        var question = getQuestion(questionId);
        question.canCreateChatRoom(directorId);

        var room = Room.of(questionId, question.getDirectorId(), question.getQuestionerId());
        roomRepository.save(room);

        question.changeQuestionStatusToChat();
        questionRepository.save(question);

        var createdRoom = roomRepository
                .findByQuestionId(questionId).orElseThrow();

        return createdRoom.getId();
    }

    private Question getQuestion(Long questionId) {
        return questionRepository
                .findByQuestionId(questionId)
                .orElseThrow(QuestionNotFoundException::new);
    }
}
