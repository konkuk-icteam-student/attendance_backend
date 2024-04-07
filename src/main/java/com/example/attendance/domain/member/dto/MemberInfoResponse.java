package com.example.attendance.domain.member.dto;

import com.example.attendance.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoResponse(
        Long Id,
        String userId,
        String userName,
        String userPhoneNum
) {
    public static MemberInfoResponse from(Member entity){
        return MemberInfoResponse.builder()
                .Id(entity.getId())
                .userId(entity.getLoginId())
                .userName(entity.getName())
                .userPhoneNum(entity.getPhoneNumber())
                .build();
    }
}
