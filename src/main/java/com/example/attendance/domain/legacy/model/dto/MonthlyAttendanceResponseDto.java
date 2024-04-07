package com.example.attendance.domain.legacy.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MonthlyAttendanceResponseDto {
    private Long id;
    private String formattedAttendanceTime; // 이 필드를 추가하세요
    private String attendanceDate;
    private String createId;
    private LocalDateTime createTime;
    private String status;
}
