package com.example.attendance.domain.legacy.model.dto;

import lombok.Getter;

@Getter
public class UserSetSemesterRequest {
    private String userId;
    private Integer year;

    private Integer semester;
}
