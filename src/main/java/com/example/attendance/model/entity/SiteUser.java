package com.example.attendance.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql의 auto increment
    private Long id;

    @Column(length = 20)
    private String userId;

    @Column(length = 20)
    private String userPw;

    @Column(length = 200)
    private String userName;

    @Column(length = 11)
    private String userPhoneNum;

    @ManyToOne
    @JsonBackReference
    private Dept dept;

    @OneToMany(mappedBy = "siteUser")
    @JsonManagedReference
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "siteUser")
    private List<TimeTable> timeTables;

    @OneToMany(mappedBy = "siteUser")
    //@JsonManagedReference
    private  List<StudentWorkSemester> studentWorkSemesterList;

}
