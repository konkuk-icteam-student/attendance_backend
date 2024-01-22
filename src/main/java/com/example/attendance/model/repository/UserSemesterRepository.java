package com.example.attendance.model.repository;

import com.example.attendance.model.entity.SiteUser;
import com.example.attendance.model.entity.StudentWorkSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSemesterRepository extends JpaRepository<StudentWorkSemester,Long> {
    //Boolean existsByYearAndSemester(Integer year,Integer semester);
}
