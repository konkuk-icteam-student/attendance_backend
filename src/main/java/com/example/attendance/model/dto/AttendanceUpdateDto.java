package com.example.attendance.model.dto;

import com.example.attendance.model.entity.Attendance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceUpdateDto {

    private Temp arriveAttendance;

    private Temp leaveAttendance;
}
