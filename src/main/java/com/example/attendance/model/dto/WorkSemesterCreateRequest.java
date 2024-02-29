package com.example.attendance.model.dto;

import lombok.Getter;

@Getter
public class WorkSemesterCreateRequest {
    private Integer year;

    private Integer semester; // 1 : 1학기, 2 : 2학기, 3 : 여름계절학기, 4 : 겨울계절학기 
}
