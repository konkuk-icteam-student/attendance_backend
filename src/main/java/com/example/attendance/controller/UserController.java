package com.example.attendance.controller;

import com.example.attendance.exception.DuplicateUserIdException;
import com.example.attendance.model.dto.*;
import com.example.attendance.model.entity.Attendance;
import com.example.attendance.model.entity.Dept;
import com.example.attendance.model.entity.SiteUser;
import com.example.attendance.model.entity.StudentWorkSemester;
import com.example.attendance.service.UserSemesterService;
import com.example.attendance.service.UserService;
import com.example.attendance.service.WebSocketService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:80")
@RequiredArgsConstructor
//@Controller
@RestController
@RequestMapping("/user")
public class UserController {
    public final UserService userService;

    //public final UserSemesterService userSemesterService;


    @PostMapping("/new-user")
    public ResponseEntity<Object> saveUser(@RequestBody UserCreateRequest request){
        try{
            UserInfo createdUser = this.userService.create(request);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (DuplicateUserIdException e) {
            return new ResponseEntity<>("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 부서가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/semester")
//    public ResponseEntity<Object> setSemester(@RequestBody UserSetSemesterRequest request){
//        try{
//            String message = this.userSemesterService.create(request);
//            return new ResponseEntity<>(message,HttpStatus.CREATED);
//        }catch(EntityNotFoundException e){
//            return new ResponseEntity<>("해당 학기가 존재하지 않습니다.", HttpStatus.CONFLICT);
//        }
//    }

//    @PostMapping("/attendance")
//    public ResponseEntity<Object> saveAttendance(@RequestBody UserAttendanceRequest request) {
//        try {
//            long startTime = System.currentTimeMillis();
//            String message = this.userService.attendanceCreate(request);
//            long stopTime = System.currentTimeMillis();
//            System.out.println("attendance duration total time :"+(stopTime - startTime));
//            return new ResponseEntity<>(message, HttpStatus.CREATED);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @PostMapping("/admin-attendance")
//    public ResponseEntity<Object> saveRequiredAttendance(@RequestBody UserAttendanceRequest request){
//        return new ResponseEntity<>(this.userService.requiredAttendanceCreate(request),HttpStatus.CREATED);
//    }

//    @GetMapping("attendance-log")
//    public ResponseEntity<List<AttendanceGetResponse>> getAttendance(){
//        List<AttendanceGetResponse> dtoList = this.userService.attendanceGet();
//        return new ResponseEntity<>(dtoList,HttpStatus.OK);
//    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        try {
            UserInfo user = userService.getUserById(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUserById(@PathVariable Long userId, @RequestBody UserUpdateRequestDto request) {
        try {
            UserInfo updatedUser = userService.updateUserById(userId, request);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return new ResponseEntity<>("사용자가 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

//    @PatchMapping("/attendance/{attendanceId}")
//    public ResponseEntity<Object> updateAttendance(@PathVariable Long attendanceId, @RequestBody UserAttendanceRequest request) {
//        try {
//            System.out.println("------------1-----------");
//            userService.updateAttendance(attendanceId, request);
//            return new ResponseEntity<>("근로시간 수정 완료", HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>("해당 출퇴근 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
//        }
//    }
//    @DeleteMapping("/attendance/{attendanceId}")
//    public ResponseEntity<Object> deleteAttendance(@PathVariable Long attendanceId) {
//        try {
//            userService.deleteAttendance(attendanceId);
//            return new ResponseEntity<>("출0 기록이 성공적으로 삭제되었습니다.", HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>("해당 출0 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
//        }
//    }


//    @GetMapping("/attendance/monthly/{userId}")
//    public ResponseEntity<Object> getUserMonthlyAttendance(@PathVariable String userId, @RequestParam int year, @RequestParam int month) {
//        try {
////            List<AttendancePairDto> monthlyAttendancePairs = userService.getUserMonthlyAttendancePairs(userId, year, month);
//            AttendanceMonthResponseDto monthlyAttendanceData = userService.getUserMonthlyAttendancePairs(userId, year, month);
//            return new ResponseEntity<>(monthlyAttendanceData, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
//        }
//    }

//    @GetMapping("/attendance/current/{deptId}")
//    public ResponseEntity<Object> getCurrentAttendanceUsers(@PathVariable Long deptId) {
//        try {
//            List<UserInfo> currentAttendanceUsers = userService.getCurrentAttendanceUsers(deptId);
//            return new ResponseEntity<>(currentAttendanceUsers, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>("해당 부서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
//        }
//    }

}
