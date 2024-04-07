package com.example.attendance.domain.legacy.model.dto;

import com.example.attendance.domain.member.dto.MemberInfoResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class DeptGetResponseDto {
    private Long id;
    private String deptName;

    private Integer workerNum;

    private String createId;

    private LocalDateTime createTime;

    private List<MemberInfoResponse> users;
}
