package com.example.attendance.domain.department.entity;

import com.example.attendance.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Dept {

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
}
