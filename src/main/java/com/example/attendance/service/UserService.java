package com.example.attendance.service;

import com.example.attendance.controller.UserController;
import com.example.attendance.controller.WebSocketController;
import com.example.attendance.exception.DuplicateUserIdException;
import com.example.attendance.model.dto.*;
import com.example.attendance.model.entity.Attendance;
import com.example.attendance.model.entity.Dept;
import com.example.attendance.model.entity.SiteUser;
import com.example.attendance.model.entity.StudentWorkSemester;
import com.example.attendance.model.repository.DeptRepository;
import com.example.attendance.model.repository.StudentWorkSemesterRepository;
import com.example.attendance.model.repository.UserAttendanceRepository;
import com.example.attendance.model.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final DeptRepository deptRepository;

    private final UserAttendanceRepository userAttendanceRepository;

    private final DeptService deptService;

    private final StudentWorkSemesterRepository studentWorkSemesterRepository;




    @Autowired
    public UserService(UserRepository userRepository,DeptRepository deptRepository,UserAttendanceRepository userAttendanceRepository, DeptService deptService, StudentWorkSemesterRepository studentWorkSemesterRepository) {
        this.userRepository = userRepository;
        this.deptRepository = deptRepository;
        this.userAttendanceRepository = userAttendanceRepository;
        this.deptService = deptService;
        this.studentWorkSemesterRepository = studentWorkSemesterRepository;


    }

    public UserInfo create(UserCreateRequest request){
        if (userRepository.existsByUserId(request.getUser_id())) {
            throw new DuplicateUserIdException("User with userId " + request.getUser_id() + " already exists");
        }

        if (!deptRepository.existsByDeptName(request.getDept())) {
            throw new EntityNotFoundException("Department with name " + request.getDept() + " not found");
        }
        SiteUser user = new SiteUser();
        user.setUserId(request.getUser_id());
        user.setUserPw(request.getUser_pw());
        user.setUserName(request.getUser_name());
        user.setUserPhoneNum(request.getUserPhoneNum());
        //부서이름으로 부서데이터 찾아서 부서 id를 user에 저장하기
        Dept dept = this.deptService.getDeptByName(request.getDept());
        user.setDept(dept);

        SiteUser newUser = this.userRepository.save(user);

        //Dto 변환
        UserInfo newUserDto = new UserInfo();
        newUserDto.setUserName(user.getUserName());
        newUserDto.setUserId(user.getUserId());
        newUserDto.setId(user.getId());
        newUserDto.setUserPhoneNum(user.getUserPhoneNum());
        return newUserDto;
    }

    public UserInfo getUserById(Long userId) {
        Optional<SiteUser> optionalUser = userRepository.findById(userId);
        UserInfo user = new UserInfo();

        if (optionalUser.isPresent()) {
            user.setId(optionalUser.get().getId());
            user.setUserId(optionalUser.get().getUserId());
            user.setUserName(optionalUser.get().getUserName());
            user.setUserPhoneNum(optionalUser.get().getUserPhoneNum());
            return user;
        } else {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
    }

    public UserInfo updateUserById(Long userId, UserUpdateRequestDto request) {
        Optional<SiteUser> optionalUser = userRepository.findById(userId);
        //SiteUser user = new SiteUser();

        if (optionalUser.isPresent()) {
            SiteUser user = optionalUser.get();
            user.setUserId(request.getUserId());
            user.setUserName(request.getUserName());
            user.setUserPhoneNum(request.getUserPhoneNum());

            userRepository.save(user);

            UserInfo updatedUser = new UserInfo();
            updatedUser.setId(user.getId());
            updatedUser.setUserId(user.getUserId());
            updatedUser.setUserName(user.getUserName());
            updatedUser.setUserPhoneNum(user.getUserPhoneNum());

            return updatedUser;
        } else {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
    }

    public void deleteUserById(Long userId) {
        Optional<SiteUser> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            SiteUser user = optionalUser.get();

            List<StudentWorkSemester> studentWorkSemesters = studentWorkSemesterRepository.findBySiteUserId(userId);
            studentWorkSemesterRepository.deleteAll(studentWorkSemesters);

            List<Attendance> atttendancelog = userAttendanceRepository.findBySiteUserId(userId);
            userAttendanceRepository.deleteAll(atttendancelog);

            userRepository.delete(user);
        } else {
            throw new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
    }


}
