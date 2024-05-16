package com.example.attendance.domain.legacy.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DeptCreateRequest {
    @Schema(description = "부서 이름", defaultValue = "정보운영팀")
    private String deptName;
    @Schema(description = "근로 인원 수", defaultValue = "20")
    private Integer workerNum;
}
