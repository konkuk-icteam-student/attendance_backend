package com.example.attendance.domain.attendance.dto;

import com.example.attendance.domain.attendance.entity.Attendance;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record AttendanceResponse(
    Long id,
    LocalDateTime attendanceTime,
    LocalDate attendanceDate,
    String createId,
    LocalDateTime createTime,
    String status
) {

    public static AttendanceResponse from(Attendance entity) {
        if (entity == null) {
            return null;
        }

        return AttendanceResponse.builder()
            .id(entity.getId())
            .attendanceTime(entity.getAttendanceTime())
            .attendanceDate(entity.getAttendanceDate())
            .createId(entity.getCreateId())
            .createTime(entity.getCreateTime())
            .status(entity.getStatus())
            .build();
    }
}
