package com.example.attendance.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "학번", defaultValue = "202011xxx")
        String loginId,
        @Schema(description = "비밀번호", defaultValue = "비밀번호")
        String password
) {
}
