package com.example.attendance.domain.legacy.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeptCreateRequest {
    private String deptName;

    private Integer workerNum;
}
