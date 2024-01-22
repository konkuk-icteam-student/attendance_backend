package com.example.attendance.model.dto;

import com.example.attendance.model.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttendancePairDto {
    private Attendance arriveAttendance;
    private Attendance leaveAttendance;
    private Duration workDuration;

    // 생성자, 게터, 세터 등이 있어야 합니다.

    // 생성자
    public AttendancePairDto(Attendance arriveAttendance, Attendance leaveAttendance) {
        this.arriveAttendance = arriveAttendance;
        this.leaveAttendance = leaveAttendance;
        calculateWorkDuration();
    }


    // Getter, Setter 등은 상황에 맞게 추가해주세요.

    // 예시로 Getter만 추가한 경우
    public Attendance getArriveAttendance() {
        return arriveAttendance;
    }

    public Attendance getLeaveAttendance() {
        return leaveAttendance;
    }

    private void calculateWorkDuration() {
        if (arriveAttendance != null && leaveAttendance != null) {
            workDuration = Duration.between(arriveAttendance.getAttendanceTime(), leaveAttendance.getAttendanceTime());
        }
        else{
            workDuration = null;
        }
    }
}
