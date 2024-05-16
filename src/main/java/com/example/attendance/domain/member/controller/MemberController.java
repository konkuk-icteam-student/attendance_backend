package com.example.attendance.domain.member.controller;

import com.example.attendance.domain.member.dto.LoginRequest;
import com.example.attendance.domain.member.dto.MemberCreateRequest;
import com.example.attendance.domain.member.dto.MemberInfoResponse;
import com.example.attendance.domain.member.dto.MemberUpdateRequest;
import com.example.attendance.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    public final MemberService memberService;

    @GetMapping("/isLogin")
    @Operation(summary = "로그인 상태 확인")
    public ResponseEntity<MemberInfoResponse> isLogin(HttpServletRequest httpRequest) {
        return ResponseEntity.ok(memberService.isLogined(httpRequest));
    }


    @PostMapping(value = "/login")
    @Operation(summary = "로그인 하기", description = "권한이 필요한 페이지를 가기전엔 반드시 로그인을 해야합니다.")
    public ResponseEntity<MemberInfoResponse> login(
            @RequestBody LoginRequest request, HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(memberService.login(request, httpRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signUp")
    @Operation(summary = "회원가입하기")
    public ResponseEntity<Object> saveMember(@RequestBody MemberCreateRequest request) {
        return ResponseEntity.ok(memberService.createMember(request));
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "회원정보 조회")
    public ResponseEntity<Object> getMemberById(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMemberById(memberId));
    }

    @PutMapping("/{memberId}")
    @Operation(summary = "회원정보 수정")
    public ResponseEntity<Object> updateUserById(@PathVariable Long memberId, @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(memberService.updateUserById(memberId, request));
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "회원정보 삭제")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long memberId) {
        memberService.deleteMemberById(memberId);
        return ResponseEntity.ok().body(null);
    }

}
