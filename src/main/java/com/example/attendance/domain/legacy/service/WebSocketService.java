package com.example.attendance.domain.legacy.service;

import com.example.attendance.domain.member.dto.MemberInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @Autowired
//    private UserService userService;

    public void sendCurrentAttendanceUsers(Long deptId, List<MemberInfoResponse> currentAttendanceUsers) {
        String destination = "/topic/currentMember/" + deptId;
        messagingTemplate.convertAndSend(destination, currentAttendanceUsers);
    }
}
