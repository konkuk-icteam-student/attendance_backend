package com.example.attendance.domain.legacy.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SemesterGetResponseDto {
    private Long Id;
    private Integer year;
    private Integer semester;
}
