package com.example.attendance.domain.attendance.entity;

import com.example.attendance.domain.BaseTimeEntity;
import com.example.attendance.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
public class Attendance extends BaseTimeEntity {

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

    @Builder
    public Attendance(Long id, LocalDateTime attendanceTime, LocalDate attendanceDate, String createId, LocalDateTime createTime, String status) {
        this.id = id;
        this.attendanceTime = attendanceTime;
        this.attendanceDate = attendanceDate;
        this.createId = createId;
        this.createTime = createTime;
        this.status = status;
    }
}
