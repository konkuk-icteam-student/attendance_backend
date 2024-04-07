package com.example.attendance.domain.member.entity;

import com.example.attendance.domain.attendance.entity.Attendance;
import com.example.attendance.domain.department.entity.Dept;
import com.example.attendance.domain.legacy.model.entity.StudentWorkSemester;
import com.example.attendance.domain.legacy.model.entity.TimeTable;
import com.example.attendance.domain.member.dto.MemberUpdateRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//mysql의 auto increment
    private Long id;

    @Column(length = 20)
    private String loginId;

    @Column(length = 20)
    private String loginPassword;

    @Column(length = 200)
    private String name;

    @Column(length = 11)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private Dept dept;

    @OneToMany(mappedBy = "member")
    private List<Attendance> attendanceList;

    @OneToMany(mappedBy = "member")
    private List<TimeTable> timeTables;

    @OneToMany(mappedBy = "member")
    private List<StudentWorkSemester> studentWorkSemesterList;

    @Builder
    public Member(String loginId, String loginPassword, String name, String phoneNumber, Role role, Dept dept) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.dept = dept;
    }

    public Member updateMemberInfo(MemberUpdateRequest request) {
        this.loginId = request.loginId();
        this.name = request.name();
        this.phoneNumber = request.phoneNumber();
        return this;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }
}
