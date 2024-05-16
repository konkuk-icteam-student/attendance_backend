package com.example.attendance.domain.legacy.service;

import com.example.attendance.domain.legacy.model.dto.UserSetSemesterRequest;
import com.example.attendance.domain.member.entity.Member;
import com.example.attendance.domain.legacy.model.entity.StudentWorkSemester;
import com.example.attendance.domain.legacy.model.entity.WorkSemester;
import com.example.attendance.domain.member.entity.MemberRepository;
import com.example.attendance.domain.legacy.model.repository.UserSemesterRepository;
import com.example.attendance.domain.legacy.model.repository.WorkSemesterRepository;
import com.example.attendance.exception.ErrorCode;
import com.example.attendance.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSemesterService {
    private final WorkSemesterRepository workSemesterRepository;
    private final MemberRepository memberRepository;

    private final UserSemesterRepository userSemesterRepository;

    public String create(UserSetSemesterRequest request){
        WorkSemester workSemester;
        if(!workSemesterRepository.existsByYearAndSemester(request.getYear(),request.getSemester()))
        {
            throw new EntityNotFoundException("semester " + request.getYear() + "."+request.getSemester()+"not found");
        }

        workSemester = workSemesterRepository.findByYearAndSemester(request.getYear(), request.getSemester());

        Member member = memberRepository.findByLoginId(request.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        StudentWorkSemester studentWorkSemester = new StudentWorkSemester();
        studentWorkSemester.setWorkSemester(workSemester);
        studentWorkSemester.setMember(member);
        userSemesterRepository.save(studentWorkSemester);

        return "학기 설정 완료";
    }


}
