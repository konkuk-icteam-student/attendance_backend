package com.example.attendance.domain.legacy.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Temp {
    private Long id;

    private LocalDateTime attendanceTime;

    private LocalDate attendanceDate;

    private String status;
}
