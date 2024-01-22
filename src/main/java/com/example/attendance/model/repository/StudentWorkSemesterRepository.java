package com.example.attendance.model.repository;

import com.example.attendance.model.entity.StudentWorkSemester;
import com.example.attendance.model.entity.WorkSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentWorkSemesterRepository extends JpaRepository<StudentWorkSemester,Long> {
    List<StudentWorkSemester> findByWorkSemesterId(Long id);
    List<StudentWorkSemester> findBySiteUserId(Long userId);
}
