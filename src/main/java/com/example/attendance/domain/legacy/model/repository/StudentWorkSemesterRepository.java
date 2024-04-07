package com.example.attendance.domain.legacy.model.repository;

import com.example.attendance.domain.legacy.model.entity.StudentWorkSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentWorkSemesterRepository extends JpaRepository<StudentWorkSemester,Long> {
    List<StudentWorkSemester> findByWorkSemesterId(Long id);
    List<StudentWorkSemester> findByMemberId(Long memberId);
}
