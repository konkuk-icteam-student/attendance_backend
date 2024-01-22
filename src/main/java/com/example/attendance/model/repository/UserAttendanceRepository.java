package com.example.attendance.model.repository;

import com.example.attendance.model.dto.AttendanceGetResponse;
import com.example.attendance.model.entity.Attendance;
import com.example.attendance.model.entity.SiteUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserAttendanceRepository extends JpaRepository<Attendance,Long> {
    List<Attendance> findBySiteUserAndAttendanceDate(SiteUser user, LocalDate attendanceDate);

    @Query("SELECT DISTINCT a FROM Attendance a LEFT JOIN FETCH a.siteUser")
    List<Attendance> findAllWithSiteUser();

    List<Attendance> findBySiteUserAndAttendanceDateBetween(SiteUser user, LocalDate startDate, LocalDate endDate);
//    ListfindBySiteUser_Id();
}
