package com.example.attendance.service;

import com.example.attendance.model.dto.*;
import com.example.attendance.model.entity.Dept;
import com.example.attendance.model.entity.SiteUser;
import com.example.attendance.model.repository.DeptRepository;
import com.example.attendance.model.repository.UserRepository;
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
    private final UserRepository userRepository;
    public Dept getDeptByName(String deptName){
        Dept dept = this.deptRepository.findByDeptName(deptName);
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

            List<UserInfo> userList = new ArrayList<>();

            for(SiteUser siteUser : resultList.get(i++).getSiteUserList())
            {
                UserInfo user1 = new UserInfo();
                user1.setUserId(siteUser.getUserId());
                user1.setUserName(siteUser.getUserName());
                user1.setUserPhoneNum(siteUser.getUserPhoneNum());
                user1.setId(siteUser.getId());
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

        List<UserInfo> userList = new ArrayList<>();
        for(SiteUser siteUser : optionalDept.get().getSiteUserList())
        {
            UserInfo user1 = new UserInfo();
            user1.setUserId(siteUser.getUserId());
            user1.setUserName(siteUser.getUserName());
            user1.setUserPhoneNum(siteUser.getUserPhoneNum());
            user1.setId(siteUser.getId());
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
            List<SiteUser> usersInDept = userRepository.findByDept(dept);
            for (SiteUser user : usersInDept) {
                user.setDept(null);
            }

            // 외래 키 제약 조건 해제
            userRepository.saveAll(usersInDept);

            deptRepository.deleteById(deptId);
        } else {
            throw new NoSuchElementException("해당 ID로 찾을 수 없는 부서입니다.");
        }
    }
}
