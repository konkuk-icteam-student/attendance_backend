package com.example.attendance.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class WorkSemester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql의 auto increment
    private Long id;

    @Column
    private Integer year;

    @Column
    private Integer semester;

    @Column(length = 20)
    private String createId;

    @Column
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "workSemester")
    @JsonManagedReference
    private List<StudentWorkSemester> studentWorkSemesterList;
}
