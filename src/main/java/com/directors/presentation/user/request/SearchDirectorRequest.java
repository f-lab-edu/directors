package com.directors.presentation.user.request;

import com.directors.domain.specialty.SpecialtyProperty;
import jakarta.validation.constraints.Size;

// 검색 파라미터 사용 플로우
// 1. 지역 넓이 -> km 단위
//    분야(전체(선택x) or n개), 스케줄 유무
// 2. text -> 닉네임, 분야 description, (자기 소개 -> 엔티티 or VO 추가 필요)
// 3. Option -> 평점(높은 순, 최소), 요청된 질문 수

public record SearchDirectorRequest(
        @Size(min = 1, max = 5, message = "지역의 범위가 1-5 사이로 입력되지 않았습니다.")
        int distance,
        SpecialtyProperty specialtyProperty,
        boolean hasSchedule,
        String searchText,
        int page,
        int size
) {
}
