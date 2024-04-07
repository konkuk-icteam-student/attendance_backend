package com.example.attendance.domain.member.controller;

import com.example.attendance.domain.legacy.model.dto.MemberCreateRequest;
import com.example.attendance.domain.member.dto.LoginRequest;
import com.example.attendance.domain.member.dto.MemberInfoResponse;
import com.example.attendance.domain.member.dto.MemberUpdateRequest;
import com.example.attendance.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    public final MemberService memberService;

    @PostMapping(value = "/login")
    public ResponseEntity<MemberInfoResponse> login(
            @RequestBody LoginRequest request, HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(memberService.login(request, httpRequest));
    }

    @PostMapping("/signUp")
    public ResponseEntity<Object> saveMember(@RequestBody MemberCreateRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<Object> getMemberById(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMemberById(memberId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUserById(@PathVariable Long userId, @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(memberService.updateUserById(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long userId) {
        memberService.deleteMemberById(userId);
        return ResponseEntity.ok().body(null);
    }

}
