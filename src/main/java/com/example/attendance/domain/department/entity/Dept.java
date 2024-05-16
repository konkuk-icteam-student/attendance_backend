package com.example.attendance.domain.department.entity;

import com.example.attendance.domain.BaseTimeEntity;
import com.example.attendance.domain.legacy.model.dto.DeptUpdateRequestDto;
import com.example.attendance.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@SuperBuilder
public class Dept extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql의 auto increment
    private Long id;

    @Column(length = 20, unique = true)
    private String deptName;

    @Column
    private Integer workerNum;

    @Column(length = 20)
    private String createId;

    @Column
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "dept")
    @JsonManagedReference
    private List<Member> memberList;

    @Builder
    public Dept(String deptName, Integer workerNum, String createId, LocalDateTime createTime) {
        this.deptName = deptName;
        this.workerNum = workerNum;
        this.createId = createId;
        this.createTime = createTime;
    }

    public void update(DeptUpdateRequestDto request) {
        this.deptName = request.getDeptName();
        this.workerNum = request.getWorkerNum();
    }
}
