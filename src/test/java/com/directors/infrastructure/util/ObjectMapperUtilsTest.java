package com.directors.infrastructure.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectMapperUtilsTest {
    @DisplayName("객체를 직렬화한다.")
    @Test
    void writeValueAsString() {
        // given
        ChatMessage message = new ChatMessage("content123", "userId11", LocalDateTime.of(2023,05,16,3,0));

        // when
        String serializedMessage = ObjectMapperUtils.writeValueAsString(message);

        // then
        assertThat(serializedMessage)
                .isEqualTo("{\"content\":\"content123\",\"senderId\":\"userId11\",\"sendTime\":[2023,5,16,3,0]}");
    }

    @DisplayName("게터 세터 없는 객체를 직렬화하면 예외가 발생한다.")
    @Test
    void writeValueAsStringWithNoGetterNoSetter() {
        // given
        ChatMessageNoGetterNoSetter message = new ChatMessageNoGetterNoSetter("content123", null, LocalDateTime.of(2023,05,16,3,0));

        // when
        assertThatThrownBy(() -> ObjectMapperUtils.writeValueAsString(message))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("InvalidDefinitionException");
    }

    @DisplayName("직렬화된 객체를 역직렬화한다.")
    @Test
    void readValue() {
        // given
        ChatMessage message = new ChatMessage("content123", "userId11", LocalDateTime.of(2023,05,16,3,0));
        String serializedMessage = ObjectMapperUtils.writeValueAsString(message);

        // when
        ChatMessage deserializedMessage = ObjectMapperUtils.readValue(serializedMessage, ChatMessage.class);

        // then
        assertThat(deserializedMessage).isInstanceOf(ChatMessage.class)
                .extracting("content", "senderId", "sendTime")
                .contains("content123", "userId11", LocalDateTime.of(2023,05,16,3,0));
    }

    @DisplayName("매개변수 없는 생성자가 없는 직렬화 객체를 역직렬화할 경우 실패한다.")
    @Test
    void readValueWithOutNoArgsConstructor() {
        // given
        String message = "{\"content\":\"content123\",\"senderId\":\"userId11\",\"sendTime\":[2023,5,16,3,0]}";

        // when then
        assertThatThrownBy(() -> ObjectMapperUtils.readValue(message, ChatMessageWithOutNoArgsConstructor.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("InvalidDefinitionException");
    }


    static class ChatMessage {
        String content;
        String senderId;
        LocalDateTime sendTime;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public LocalDateTime getSendTime() {
            return sendTime;
        }

        public void setSendTime(LocalDateTime sendTime) {
            this.sendTime = sendTime;
        }

        public ChatMessage(String content, String senderId, LocalDateTime sendTime) {
            this.content = content;
            this.senderId = senderId;
            this.sendTime = sendTime;
        }

        public ChatMessage() {
         }
    }

    static class ChatMessageNoGetterNoSetter {
        String content;
        String senderId;
        LocalDateTime sendTime;

        public ChatMessageNoGetterNoSetter(String content, String senderId, LocalDateTime sendTime) {
            this.content = content;
            this.senderId = senderId;
            this.sendTime = sendTime;
        }
    }

    static class ChatMessageWithOutNoArgsConstructor {
        String content;
        String senderId;
        LocalDateTime sendTime;

        public ChatMessageWithOutNoArgsConstructor(String content, String senderId, LocalDateTime sendTime) {
            this.content = content;
            this.senderId = senderId;
            this.sendTime = sendTime;
        }
    }
}