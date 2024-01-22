package com.example.attendance.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @ManyToOne
    @JoinColumn(name = "siteUser_id")
    @JsonBackReference
    private SiteUser siteUser;

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
