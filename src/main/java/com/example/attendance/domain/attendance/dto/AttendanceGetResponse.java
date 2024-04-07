package com.example.attendance.domain.attendance.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
