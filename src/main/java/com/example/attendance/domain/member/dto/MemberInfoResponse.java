package com.example.attendance.domain.member.dto;

import com.example.attendance.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MemberInfoResponse(
        @Schema(description = "회원 pk", defaultValue = "1")
        Long Id,
        @Schema(description = "학번", defaultValue = "202011xxx")
        String loginId,
        @Schema(description = "회원 이름", defaultValue = "홍길동")
        String name,
        @Schema(description = "전화번호", defaultValue = "010-xxxx-xxxx")
        String phoneNumber,
        @Schema(description = "유저의 권한입니다. 권한 종류는 ROLE_MEMBER, ROLE_ADMIN 2가지 입니다", defaultValue = "ROLE_MEMBER")
        String role
) {
    public static MemberInfoResponse from(Member entity) {
        return MemberInfoResponse.builder()
                .Id(entity.getId())
                .loginId(entity.getLoginId())
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .role(entity.getRole().getKey())
                .build();
    }

}
