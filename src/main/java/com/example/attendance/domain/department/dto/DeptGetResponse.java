package com.example.attendance.domain.department.dto;

import com.example.attendance.domain.department.entity.Dept;
import com.example.attendance.domain.member.dto.MemberInfoResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record DeptGetResponse(
        Long id,
        String deptName,
        Integer workerNum,
        String createId,
        LocalDateTime createTime,
        List<MemberInfoResponse> users
) {
    public static DeptGetResponse from(Dept dept) {
        return DeptGetResponse.builder()
                .id(dept.getId())
                .deptName(dept.getDeptName())
                .createId(dept.getCreateId())
                .workerNum(dept.getWorkerNum())
                .users(dept.getMemberList().stream()
                        .map(member -> MemberInfoResponse.from(member))
                        .collect(Collectors.toList()))
                .build();
    }
}
