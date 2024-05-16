//package com.example.attendance.controller;
//
//import com.example.attendance.model.dto.DeptIdRequest;
//import com.example.attendance.model.dto.UserInfo;
//import com.example.attendance.service.UserService;
//import com.example.attendance.service.WebSocketService;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Repository;
//
//import java.util.Collections;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Controller
//public class WebSocketController {
//    public final UserService userService;
//    @MessageMapping("/send-attendance")
//    @SendTo("/topic/attendance/dept/{deptId}")
//    public List<UserInfo> sendAttendance(@Payload DeptIdRequest deptIdRequest) {
//        Long deptId = deptIdRequest.getDeptId();
//        try {
//            List<UserInfo> currentAttendanceUsers = userService.getCurrentAttendanceUsers(deptId);
//            WebSocketService webSocketService = null;
//            webSocketService.sendCurrentAttendanceUsers(deptId, currentAttendanceUsers);
//            return currentAttendanceUsers;
//        } catch (EntityNotFoundException e) {
//            return Collections.emptyList();
//        }
//    }
//}
package com.example.attendance.domain.legacy.controller;

import com.example.attendance.domain.member.dto.MemberInfoResponse;
import com.example.attendance.domain.attendance.service.AttendanceService;
import com.example.attendance.domain.member.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class WebSocketController {

    public final AttendanceService attendanceService;
    public final MemberService memberService;
//    @MessageMapping("/dept")
//    @SendTo("/topic/currentMember/{deptId}")
//    public List<UserInfo> sendAttendance(@Payload DeptIdRequest deptIdRequest,@DestinationVariable Long deptId) {
//        try {
//            List<UserInfo> currentAttendanceUsers = userService.getCurrentAttendanceUsers(deptIdRequest.getDeptId());
//            return currentAttendanceUsers;
//        } catch (EntityNotFoundException e) {
//            return Collections.emptyList();
//        }
//    }
//    @MessageMapping("/dept/{deptId}")
//    @SendTo("/topic/currentMember/{deptId}")
//    public List<UserInfo> sendAttendance(@Payload DeptIdRequest deptIdRequest) {
//        try {
//            List<UserInfo> currentAttendanceUsers = userService.getCurrentAttendanceUsers(deptIdRequest.getDeptId());
//            System.out.println(deptIdRequest.getDeptId());
//            System.out.println("--------send code----------");
//            return currentAttendanceUsers;
//        } catch (EntityNotFoundException e) {
//            System.out.println("--------is error----------");
//            return Collections.emptyList();
//        }
//    }
@MessageMapping("/dept/{deptId}")
@SendTo("/topic/currentMember/{deptId}")
public List<MemberInfoResponse> sendAttendance(@DestinationVariable Long deptId) {
    try {

        List<MemberInfoResponse> currentAttendanceUsers = attendanceService.getCurrentAttendanceUsers(deptId);
        System.out.println(deptId);
        System.out.println("--------send code----------");
        return currentAttendanceUsers;
    } catch (EntityNotFoundException e) {
        System.out.println("--------is error----------");
        return Collections.emptyList();
    }
}

    //@MessageMapping("/dept/{deptId}")
//    @SendTo("/topic/currentMember/1")
//    public List<UserInfo> sendAttendance2(Long deptId2) {
//        try {
//            List<UserInfo> currentAttendanceUsers = userService.getCurrentAttendanceUsers(deptId2);
//            System.out.println(deptId2);
//            System.out.println("--------send code----------");
//            return currentAttendanceUsers;
//        } catch (EntityNotFoundException e) {
//            System.out.println("--------is error----------");
//            return Collections.emptyList();
//        }
//    }
}

