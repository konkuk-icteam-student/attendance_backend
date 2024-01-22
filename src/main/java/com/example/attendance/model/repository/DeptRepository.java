package com.example.attendance.model.repository;

import com.example.attendance.model.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptRepository extends JpaRepository<Dept,Long> {
    Dept findByDeptName(String deptName);
    Boolean existsByDeptName(String deptName);
}
