package com.example.attendance.domain.attendance.dto;

import com.example.attendance.domain.legacy.model.dto.Temp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceUpdateDto {

    private Temp arriveAttendance;

    private Temp leaveAttendance;
}
