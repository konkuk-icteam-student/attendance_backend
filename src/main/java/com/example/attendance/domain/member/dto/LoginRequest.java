package com.example.attendance.domain.member.dto;

public record LoginRequest(
        String loginId,
        String password
) {
}
