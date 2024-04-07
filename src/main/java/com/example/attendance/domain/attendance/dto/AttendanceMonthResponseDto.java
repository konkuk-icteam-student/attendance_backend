package com.example.attendance.domain.attendance.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Setter
@Getter
public class AttendanceMonthResponseDto {
    private List<AttendancePairDto> attendanceDataList;

    private Duration totalDuration;
}
