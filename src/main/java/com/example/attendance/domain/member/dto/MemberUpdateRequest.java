package com.example.attendance.domain.member.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record MemberUpdateRequest(
        @Schema(description = "학번", defaultValue = "202011xxx")
        String loginId,
        @Schema(description = "회원 이름", defaultValue = "홍길동")
        String name,
        @Schema(description = "전화번호", defaultValue = "010-xxxx-xxxx")
        String phoneNumber
) {

}
