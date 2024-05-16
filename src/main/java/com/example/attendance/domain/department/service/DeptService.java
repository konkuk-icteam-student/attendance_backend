package com.example.attendance.domain.department.service;

import com.example.attendance.domain.department.dto.DeptGetResponse;
import com.example.attendance.domain.department.entity.Dept;
import com.example.attendance.domain.legacy.model.dto.DeptCreateRequest;
import com.example.attendance.domain.legacy.model.dto.DeptUpdateRequestDto;
import com.example.attendance.domain.legacy.model.dto.DeptUpdateResponseDto;
import com.example.attendance.domain.legacy.model.repository.DeptRepository;
import com.example.attendance.domain.member.entity.Member;
import com.example.attendance.domain.member.entity.MemberRepository;
import com.example.attendance.exception.DuplicateException;
import com.example.attendance.exception.ErrorCode;
import com.example.attendance.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeptService {

    private final DeptRepository deptRepository;
    private final MemberRepository memberRepository;

    public Dept getDeptByName(String deptName) {
        Dept dept = this.deptRepository.findByDeptName(deptName)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DEPT_NOT_FOUND));
        return dept;
    }

    public Dept create(DeptCreateRequest request) {
        if (deptRepository.existsByDeptName(request.getDeptName())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_DEPT_NAME);
        }

        return deptRepository.save(Dept.builder()
                .deptName(request.getDeptName())
                .workerNum(request.getWorkerNum())
                .build());
    }

    /**
     * 존재하는 모든 부서의 정보를 return 합니다.
     *
     * @return
     */
    @Transactional
    public List<DeptGetResponse> getAllDepartments() {
        return deptRepository.findAll().stream()
                .map(dept -> DeptGetResponse.from(dept))
                .collect(Collectors.toList());
    }

    /**
     * 특정 id의 부서 정보를 return 합니다.
     *
     * @param deptId
     * @return
     */
    @Transactional
    public DeptGetResponse getDepartments(Long deptId) {
        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DEPT_NOT_FOUND));
        return DeptGetResponse.from(dept);
    }

    public DeptUpdateResponseDto update(Long deptId, DeptUpdateRequestDto request) {
        Dept dept = deptRepository.findById(deptId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DEPT_NOT_FOUND));

        dept.update(request);

        deptRepository.save(dept);

        // 엔터티를 저장하고 업데이트된 엔터티를 반환
        DeptUpdateResponseDto newDept = new DeptUpdateResponseDto();
        newDept.setDeptName(dept.getDeptName());
        newDept.setWorkerNum(dept.getWorkerNum());

        return newDept;
    }

    public void delete(Long deptId) {
        Optional<Dept> optionalDept = deptRepository.findById(deptId);

        if (optionalDept.isPresent()) {
            Dept dept = optionalDept.get();
            // 연결된 사용자의 부서 정보를 null로 설정
            List<Member> usersInDept = memberRepository.findByDept(dept);
            for (Member user : usersInDept) {
                user.setDept(null);
            }

            // 외래 키 제약 조건 해제
            memberRepository.saveAll(usersInDept);

            deptRepository.deleteById(deptId);
        } else {
            throw new NoSuchElementException("해당 ID로 찾을 수 없는 부서입니다.");
        }
    }
}
