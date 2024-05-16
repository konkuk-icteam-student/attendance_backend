package com.example.attendance.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MemberCreateRequest(
        @NotNull
        @Schema(description = "학번", defaultValue = "202011xxx")
        String loginId,
        @NotNull
        @Schema(description = "비밀번호", defaultValue = "비밀번호")
        String password,
        @NotNull
        @Schema(description = "회원 이름", defaultValue = "홍길동")
        String name,
        @NotNull
        @Schema(description = "전화번호", defaultValue = "010-xxxx-xxxx")
        String phoneNumber,
        @NotNull
        @Schema(description = "부서명", defaultValue = "정보운영팀")
        String department
) {


}
