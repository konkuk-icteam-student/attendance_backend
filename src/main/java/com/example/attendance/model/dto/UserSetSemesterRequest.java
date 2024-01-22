package com.example.attendance.model.dto;

import lombok.Getter;

@Getter
public class UserSetSemesterRequest {
    private String userId;
    private Integer year;

    private Integer semester;
}
