package com.example.attendance.domain.member.dto;


public record MemberUpdateRequest(
    String loginId,
    String name,
    String phoneNumber
) {

}
