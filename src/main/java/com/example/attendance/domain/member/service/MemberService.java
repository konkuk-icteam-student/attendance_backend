package com.example.attendance.domain.member.service;

import com.example.attendance.domain.attendance.entity.Attendance;
import com.example.attendance.domain.attendance.entity.AttendanceRepository;
import com.example.attendance.domain.department.entity.Dept;
import com.example.attendance.domain.legacy.model.dto.MemberCreateRequest;
import com.example.attendance.domain.legacy.model.entity.StudentWorkSemester;
import com.example.attendance.domain.legacy.model.repository.DeptRepository;
import com.example.attendance.domain.legacy.model.repository.StudentWorkSemesterRepository;
import com.example.attendance.domain.member.dto.LoginRequest;
import com.example.attendance.domain.member.dto.MemberInfoResponse;
import com.example.attendance.domain.member.dto.MemberUpdateRequest;
import com.example.attendance.domain.member.entity.Member;
import com.example.attendance.domain.member.entity.MemberRepository;
import com.example.attendance.exception.DuplicateUserIdException;
import com.example.attendance.exception.ErrorCode;
import com.example.attendance.exception.NotEqualsException;
import com.example.attendance.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DeptRepository deptRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentWorkSemesterRepository studentWorkSemesterRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인시 해당 요청과 저장된 아이디와 비밀번호를 대조하여 로그인 성공을 알립니다.
     *
     * @param request
     * @param httpRequest
     * @return
     */
    public MemberInfoResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        Member member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isPasswordMatches = passwordEncoder.matches(member.getLoginPassword(), request.password());
        if (isPasswordMatches) {

            HttpSession session = httpRequest.getSession();
            session.setAttribute("loginId", request.loginId());

            return MemberInfoResponse.from(member);
        } else {
            throw new NotEqualsException(ErrorCode.PASSWORD_INCORRECT);
        }

    }

    /**
     * 회원 가입 정보를 바탕으로 새로운 멤버를 생성합니다.
     *
     * @param request 클래스를 생성하기위한 정보들이 들어있습니다. {@link MemberCreateRequest}
     * @return {@link MemberInfoResponse}
     */
    public MemberInfoResponse createMember(MemberCreateRequest request) {
        if (memberRepository.existsByLoginId(request.getUser_id())) {
            throw new DuplicateUserIdException("User with userId " + request.getUser_id() + " already exists");
        }

        Dept dept = deptRepository.findByDeptName(request.getDept())
                .orElseThrow(() -> new NotFoundException(ErrorCode.DEPT_NOT_FOUND));

        Member member = memberRepository.save(Member.builder()
                .loginId(request.getUser_id())
                .loginPassword(request.getUser_pw())
                .name(request.getUser_name())
                .phoneNumber(request.getUserPhoneNum())
                .dept(dept)
                .build()
        );

        return MemberInfoResponse.from(member);
    }

    /**
     * pk를 이용해 멤버를 조회합니다.
     *
     * @param memberId
     * @return {@link MemberInfoResponse}
     */
    public MemberInfoResponse getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberInfoResponse.from(member);

    }

    /**
     * 해당 회원의 정보를 수정합니다.
     *
     * @param memberId
     * @param request
     * @return {@link MemberInfoResponse}
     */
    @Transactional
    public MemberInfoResponse updateUserById(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateMemberInfo(request);
        memberRepository.save(member);

        return MemberInfoResponse.from(member);

    }

    public void deleteMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        List<StudentWorkSemester> studentWorkSemesters = studentWorkSemesterRepository.findByMemberId(memberId);
        studentWorkSemesterRepository.deleteAll(studentWorkSemesters);

        List<Attendance> atttendancelog = attendanceRepository.findByMemberId(memberId);
        attendanceRepository.deleteAll(atttendancelog);

        memberRepository.delete(member);

    }


}
