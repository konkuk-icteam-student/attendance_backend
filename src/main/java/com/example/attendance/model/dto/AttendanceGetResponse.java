package com.example.attendance.model.dto;

import com.example.attendance.model.entity.Attendance;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class AttendanceGetResponse {
    private LocalDate attendanceDate;
    private LocalDateTime attendanceTime;
    //private String createId;
    //private LocalDateTime createTime;

    private String UserId;
    private String deptName;
    //private String userId;
    private String userName;
    //private String userPw;
}
