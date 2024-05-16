package com.example.attendance.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "Sample Error Message"),

    // Common
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "요청 한 값 타입이 잘못되어 binding에 실패하였습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP method 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류, 관리자에게 문의하세요"),


    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    MEMBER_NOT_AUTHORIZED_TO_ANNOUNCE(HttpStatus.BAD_REQUEST, "이 회원은 해당 공지를 할 권한이 없습니다."),
    IS_NOT_TEACHER(HttpStatus.BAD_REQUEST, "해당 회원은 선생님 회원이 아닙니다"),
    IS_NOT_STUDENT(HttpStatus.BAD_REQUEST, "해당 회원은 학생 회원이 아닙니다"),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    LOGINID_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 학번입니다"),
    IS_NOT_LOGINED(HttpStatus.BAD_REQUEST, "현재 로그인 중이 아닙니다."),

    //Dept
    DEPT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 부서를 찾을 수 없습니다."),
    DUPLICATE_DEPT_NAME(HttpStatus.BAD_REQUEST, "이미 존재하는 부서명입니다."),

    // Security
    ID_OR_PW_EMPTY(HttpStatus.BAD_REQUEST, "아이디나 비밀번호가 비어있습니다"),

    AUTH_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보를 찾을 수 없습니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    MEMBER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 가입된 회원입니다."),
    MEMBER_ALREADY_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    MEMBER_INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "올바르지 않는 닉네임입니다."),
    MEMBER_ALREADY_DELETED(HttpStatus.NOT_FOUND, "이미 탈퇴한 회원입니다."),
    PASSWORD_NOT_MATCHES(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    ID_TOKEN_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "ID 토큰 검증에 실패했습니다."),

    // Image
    IMAGE_KEY_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지 키를 찾을 수 없습니다."),
    IMAGE_FILE_EXTENSION_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지 파일 형식을 찾을 수 없습니다."),
    IMAGE_TYPE_INCORRECT(HttpStatus.BAD_REQUEST, "해당 이미지 타입을 받을 수 없습니다."),
    IMAGEFILE_AND_REQUESTBODY_COUNT_NOT_EQUALS(HttpStatus.BAD_REQUEST, "파일 개수와 요청 body의 개수가 맞지 않습니다."),

    // Notification
    SELF_SENDING_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "본인에게 메세지를 전송할 수 없습니다."),
    TODAY_COMPLETED_MISSION_SENDING_NOT_ALLOWED(
            HttpStatus.BAD_REQUEST, "오늘 미션을 완료한 미션에는 메세지를 전송할 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String message;
}

