package com.example.attendance.domain.legacy.model.repository;

import com.example.attendance.domain.legacy.model.entity.StudentWorkSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSemesterRepository extends JpaRepository<StudentWorkSemester,Long> {
    //Boolean existsByYearAndSemester(Integer year,Integer semester);
}
