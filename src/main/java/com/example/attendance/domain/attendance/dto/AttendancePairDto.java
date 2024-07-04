package com.example.attendance.domain.attendance.dto;

import com.example.attendance.domain.attendance.entity.Attendance;
import java.time.Duration;
import lombok.Getter;

@Getter
public class AttendancePairDto {

    private AttendanceResponse arriveAttendance;
    private AttendanceResponse leaveAttendance;
    private Duration workDuration;

    public AttendancePairDto(Attendance arriveAttendance, Attendance leaveAttendance) {
        this.arriveAttendance = AttendanceResponse.from(arriveAttendance);
        this.leaveAttendance = AttendanceResponse.from(leaveAttendance);
        calculateWorkDuration();
    }

    private void calculateWorkDuration() {
        if (arriveAttendance != null && leaveAttendance != null) {
            workDuration = Duration.between(arriveAttendance.attendanceTime(), leaveAttendance.attendanceTime());
        } else {
            workDuration = null;
        }
    }
}
