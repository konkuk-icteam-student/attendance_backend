package com.example.attendance.controller;

import com.example.attendance.model.dto.AttendanceGetResponse;
import com.example.attendance.model.dto.AttendanceMonthResponseDto;
import com.example.attendance.model.dto.UserAttendanceRequest;
import com.example.attendance.model.dto.UserInfo;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AttendanceController {
    public final AttendanceService attendanceService;

    @PostMapping("/attendance")
    public ResponseEntity<Object> saveAttendance(@RequestBody UserAttendanceRequest request) {
        try {
            long startTime = System.currentTimeMillis();
            String message = this.attendanceService.attendanceCreate(request);
            long stopTime = System.currentTimeMillis();
            System.out.println("attendance duration total time :"+(stopTime - startTime));
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin-attendance")
    public ResponseEntity<Object> saveRequiredAttendance(@RequestBody UserAttendanceRequest request){
        return new ResponseEntity<>(this.attendanceService.requiredAttendanceCreate(request),HttpStatus.CREATED);
    }

    @GetMapping("attendance-log")
    public ResponseEntity<List<AttendanceGetResponse>> getAttendance(){
        List<AttendanceGetResponse> dtoList = this.attendanceService.attendanceGet();
        return new ResponseEntity<>(dtoList,HttpStatus.OK);
    }

    @PatchMapping("/attendance/{attendanceId}")
    public ResponseEntity<Object> updateAttendance(@PathVariable Long attendanceId, @RequestBody UserAttendanceRequest request) {
        try {
            System.out.println("------------1-----------");
            attendanceService.updateAttendance(attendanceId, request);
            return new ResponseEntity<>("근로시간 수정 완료", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 출퇴근 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/attendance/{attendanceId}")
    public ResponseEntity<Object> deleteAttendance(@PathVariable Long attendanceId) {
        try {
            attendanceService.deleteAttendance(attendanceId);
            return new ResponseEntity<>("출0 기록이 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 출0 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/attendance/monthly/{userId}")
    public ResponseEntity<Object> getUserMonthlyAttendance(@PathVariable String userId, @RequestParam int year, @RequestParam int month) {
        try {
//            List<AttendancePairDto> monthlyAttendancePairs = userService.getUserMonthlyAttendancePairs(userId, year, month);
            AttendanceMonthResponseDto monthlyAttendanceData = attendanceService.getUserMonthlyAttendancePairs(userId, year, month);
            return new ResponseEntity<>(monthlyAttendanceData, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/attendance/current/{deptId}")
    public ResponseEntity<Object> getCurrentAttendanceUsers(@PathVariable Long deptId) {
        try {
            List<UserInfo> currentAttendanceUsers = attendanceService.getCurrentAttendanceUsers(deptId);
            return new ResponseEntity<>(currentAttendanceUsers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 부서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }


}
