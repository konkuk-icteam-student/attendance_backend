package com.example.attendance.domain.legacy.model.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeptUpdateRequestDto {
    private String deptName;

    private Integer workerNum;
}
