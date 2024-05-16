package com.example.attendance.domain.attendance.controller;

import com.example.attendance.domain.attendance.dto.AttendanceGetResponse;
import com.example.attendance.domain.attendance.dto.AttendanceMonthResponseDto;
import com.example.attendance.domain.attendance.service.AttendanceService;
import com.example.attendance.domain.legacy.model.dto.UserAttendanceRequest;
import com.example.attendance.domain.member.dto.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
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
        System.out.println();
<<<<<<< HEAD:src/main/java/com/example/attendance/controller/AttendanceController.java
=======
        System.out.println();
>>>>>>> origin/temp:src/main/java/com/example/attendance/domain/attendance/controller/AttendanceController.java
        try {
            long startTime = System.currentTimeMillis();
            String message = this.attendanceService.attendanceCreate(request);
            long stopTime = System.currentTimeMillis();
            System.out.println("attendance duration total time :" + (stopTime - startTime));
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin-attendance")
    public ResponseEntity<Object> saveRequiredAttendance(@RequestBody UserAttendanceRequest request) {
        return new ResponseEntity<>(this.attendanceService.requiredAttendanceCreate(request), HttpStatus.CREATED);
    }

    @GetMapping("attendance-log")
    public ResponseEntity<List<AttendanceGetResponse>> getAttendance() {
        List<AttendanceGetResponse> dtoList = this.attendanceService.attendanceGet();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PutMapping("/attendance/{attendanceId}")
    @Operation(summary = "출근 시간 수정하기")
    public ResponseEntity<Object> updateAttendance(@PathVariable Long attendanceId, @RequestBody UserAttendanceRequest request) {
        try {
            attendanceService.updateAttendance(attendanceId, request);
            return ResponseEntity.ok().body(null);
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
//            List<AttendancePairDto> monthlyAttendancePairs = userService.getUserMonthlyAttendancePairs(loginId, year, month);
            AttendanceMonthResponseDto monthlyAttendanceData = attendanceService.getUserMonthlyAttendancePairs(userId, year, month);
            return new ResponseEntity<>(monthlyAttendanceData, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/attendance/current/{deptId}")
    public ResponseEntity<Object> getCurrentAttendanceUsers(@PathVariable Long deptId) {
        try {
            List<MemberInfoResponse> currentAttendanceUsers = attendanceService.getCurrentAttendanceUsers(deptId);
            return new ResponseEntity<>(currentAttendanceUsers, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("해당 부서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
    }


}
