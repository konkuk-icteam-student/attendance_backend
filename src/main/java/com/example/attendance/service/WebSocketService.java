package com.example.attendance.service;

import com.example.attendance.model.dto.UserInfo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @Autowired
//    private UserService userService;

    public void sendCurrentAttendanceUsers(Long deptId, List<UserInfo> currentAttendanceUsers) {
        String destination = "/topic/attendance/dept/" + deptId;
        messagingTemplate.convertAndSend(destination, currentAttendanceUsers);
    }
}
