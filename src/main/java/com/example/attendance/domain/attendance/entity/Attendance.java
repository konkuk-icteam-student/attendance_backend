package com.example.attendance.domain.attendance.entity;

import com.example.attendance.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql의 auto increment
    private Long id;

    @NotNull(message = "사용자는 Null 일 수 없습니다!")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private LocalDateTime attendanceTime;

    @Column
    private LocalDate attendanceDate;

//    @Column
//    private boolean check;

    @Column(length = 20)
    private String createId;

    @Column
    private LocalDateTime createTime;

    //새로 추가
    @Column
    private String status;
}
