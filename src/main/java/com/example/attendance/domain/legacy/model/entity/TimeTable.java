package com.example.attendance.domain.legacy.model.entity;

import com.example.attendance.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql의 auto increment
    private Long id;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column(length = 20)
    private String createId;

    @Column
    private LocalDateTime createTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
