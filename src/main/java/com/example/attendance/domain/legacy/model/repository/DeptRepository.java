package com.example.attendance.domain.legacy.model.repository;

import com.example.attendance.domain.department.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeptRepository extends JpaRepository<Dept,Long> {
    Optional<Dept> findByDeptName(String deptName);
    Boolean existsByDeptName(String deptName);
}
