package com.example.attendance.domain.attendance.dto;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttendanceMonthResponseDto {

    private List<AttendancePairDto> attendanceDataList;
    private Duration totalDuration;
}
