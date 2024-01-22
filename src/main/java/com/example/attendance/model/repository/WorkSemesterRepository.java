package com.example.attendance.model.repository;

import com.example.attendance.model.entity.WorkSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSemesterRepository extends JpaRepository<WorkSemester,Long> {
    //Boolean findByYearAndSemester(String year,String semester);
    Boolean existsByYearAndSemester(Integer year,Integer semester);

    WorkSemester findByYearAndSemester(Integer year, Integer semester);
}
