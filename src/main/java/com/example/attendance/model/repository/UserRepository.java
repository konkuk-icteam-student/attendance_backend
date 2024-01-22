package com.example.attendance.model.repository;

import com.example.attendance.model.dto.AttendanceGetResponse;
import com.example.attendance.model.entity.Dept;
import com.example.attendance.model.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<SiteUser,Long> {
    Boolean existsByUserId(String userId);

    SiteUser findByUserId(String userId);

    List<SiteUser> findByDept(Dept department);
}
