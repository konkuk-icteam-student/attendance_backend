package com.example.attendance.domain.department.service;

import com.example.attendance.domain.legacy.model.dto.*;
import com.example.attendance.domain.department.entity.Dept;
import com.example.attendance.domain.member.dto.MemberInfoResponse;
import com.example.attendance.domain.member.entity.Member;
import com.example.attendance.domain.legacy.model.repository.DeptRepository;
import com.example.attendance.domain.member.entity.MemberRepository;
import com.example.attendance.exception.ErrorCode;
import com.example.attendance.exception.NotFoundException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeptService {

    private final DeptRepository deptRepository;
    private final MemberRepository memberRepository;
    public Dept getDeptByName(String deptName){
        Dept dept = this.deptRepository.findByDeptName(deptName)
                .orElseThrow(()-> new NotFoundException(ErrorCode.DEPT_NOT_FOUND));
        return dept;
    }

    public Dept create(DeptCreateRequest request){
        if (deptRepository.existsByDeptName(request.getDeptName())) {
            throw new EntityExistsException("Department with name " + request.getDeptName() + " already exists");
        }

        Dept dept = new Dept();
        dept.setDeptName(request.getDeptName());
        dept.setWorkerNum(request.getWorkerNum());
        //생성자와 시간은 임시로 기본값으로 테스트
        dept.setCreateId(null);
        dept.setCreateTime(null);
        return this.deptRepository.save(dept);
    }

//    public List getAllDepartments(){
//        List<Dept> resultList = deptRepository.findAll();
//        List<DeptGetResponseDto> deptList = new ArrayList<>();
//        for(Dept dept : resultList){
//            DeptGetResponseDto dto = new DeptGetResponseDto();
//            dto.setId(dept.getId());
//            dto.setDeptName(dept.getDeptName());
//            dto.setWorkerNum(dept.getWorkerNum());
//            dto.setCreateId(dept.getCreateId());
//            dto.setCreateTime(dept.getCreateTime());
//            deptList.add(dto);
//        }
//        return deptList;
//    }
    @Transactional
    public List getAllDepartments(){
        List<Dept> resultList = deptRepository.findAll();

        List<DeptGetResponseDto> deptList = new ArrayList<>();
        int i = 0;

        for(Dept dept : resultList){
            DeptGetResponseDto dto = new DeptGetResponseDto();
            dto.setId(dept.getId());
            dto.setDeptName(dept.getDeptName());
            dto.setWorkerNum(dept.getWorkerNum());
            dto.setCreateId(dept.getCreateId());
            dto.setCreateTime(dept.getCreateTime());

            List<MemberInfoResponse> userList = new ArrayList<>();

            for(Member member : resultList.get(i++).getMemberList())
            {
                MemberInfoResponse user1 = MemberInfoResponse.from(member);
                userList.add(user1);
            }
            dto.setUsers(userList);
            deptList.add(dto);
        }
        return deptList;
    }

    @Transactional
    public DeptGetResponseDto getDepartments(Long deptId){
        Optional<Dept> optionalDept = deptRepository.findById(deptId);

        DeptGetResponseDto dept = new DeptGetResponseDto();

        dept.setId(optionalDept.get().getId());
        dept.setDeptName(optionalDept.get().getDeptName());
        dept.setWorkerNum(optionalDept.get().getWorkerNum());
        dept.setCreateTime(optionalDept.get().getCreateTime());
        dept.setCreateId(optionalDept.get().getCreateId());

        List<MemberInfoResponse> userList = new ArrayList<>();
        for(Member member : optionalDept.get().getMemberList())
        {
            MemberInfoResponse user1 = MemberInfoResponse.from(member);
            userList.add(user1);
        }
        dept.setUsers(userList);

        return dept;
    }

    public DeptUpdateResponseDto update(Long deptId, DeptUpdateRequestDto request) {
        Dept existingDept = deptRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("부서를 찾을 수 없습니다."));

        if(deptRepository.existsByDeptName(request.getDeptName())){
            throw new EntityExistsException("이미 존재하는 부서 이름입니다.");
        }

        existingDept.setDeptName(request.getDeptName());
        existingDept.setWorkerNum(request.getWorkerNum());
        existingDept.setCreateId(null);
        existingDept.setCreateTime(null);

        deptRepository.save(existingDept);

        // 엔터티를 저장하고 업데이트된 엔터티를 반환
        DeptUpdateResponseDto newDept = new DeptUpdateResponseDto();
        newDept.setDeptName(existingDept.getDeptName());
        newDept.setWorkerNum(existingDept.getWorkerNum());

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
