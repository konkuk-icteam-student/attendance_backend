package com.example.attendance.domain.member.entity;

import com.example.attendance.domain.department.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Boolean existsByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findByDept(Dept department);
}
