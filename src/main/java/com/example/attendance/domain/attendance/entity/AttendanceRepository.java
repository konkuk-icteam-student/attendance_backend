package com.example.attendance.domain.attendance.entity;

import com.example.attendance.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findByMemberAndAttendanceDate(Member member, LocalDate attendanceDate);

    @Query("SELECT DISTINCT a FROM Attendance a LEFT JOIN FETCH a.member")
    List<Attendance> findAllWithMember();

    List<Attendance> findByMemberAndAttendanceDateBetween(Member member, LocalDate startDate, LocalDate endDate);

    List<Attendance> findByMemberId(Long id);

}
