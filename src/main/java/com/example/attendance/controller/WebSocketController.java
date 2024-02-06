package com.example.attendance.controller;

import com.example.attendance.model.dto.DeptIdRequest;
import com.example.attendance.model.dto.UserInfo;
import com.example.attendance.service.UserService;
import com.example.attendance.service.WebSocketService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class WebSocketController {
    public final UserService userService;
    @MessageMapping("/send-attendance")
    @SendTo("/topic/attendance/dept/{deptId}")
    public List<UserInfo> sendAttendance(@Payload DeptIdRequest deptIdRequest) {
        Long deptId = deptIdRequest.getDeptId();
        try {
            List<UserInfo> currentAttendanceUsers = userService.getCurrentAttendanceUsers(deptId);
            WebSocketService webSocketService = null;
            webSocketService.sendCurrentAttendanceUsers(deptId, currentAttendanceUsers);
            return currentAttendanceUsers;
        } catch (EntityNotFoundException e) {
            return Collections.emptyList();
        }
    }
}
