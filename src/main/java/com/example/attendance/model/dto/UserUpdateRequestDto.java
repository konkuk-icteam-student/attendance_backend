package com.example.attendance.model.dto;

import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    private String userId;

    private String userName;

    private String userPhoneNum;
}
