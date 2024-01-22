package com.example.attendance.model.dto;

import com.example.attendance.model.entity.StudentWorkSemester;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class SemesterGetResponseDto {
    private Long Id;
    private Integer year;
    private Integer semester;
}
