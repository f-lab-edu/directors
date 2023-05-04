package com.directors.presentation.question.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateQuestionRequest {
    @NotBlank(message = "디렉터 아이디가 입력되지 않았습니다.")
    private String directorId;
    @Size(min = 2, message = "제목의 2글자 이상 작성해야합니다.")
    @NotBlank(message = "제목이 입력되지 않았습니다.")
    private String title;
    @Size(min = 10, message = "내용은 10글자 이상 작성해야합니다.")
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String content;
    @Future(message = "약속 시간은 현재 시간보다 이후 시간대로 지정해야합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "예약시간이 입력되지 않았습니다.")
    private LocalDateTime startTime;
    @NotBlank(message = "카테고리가 입력되지 않았습니다.")
    private String category;
}
