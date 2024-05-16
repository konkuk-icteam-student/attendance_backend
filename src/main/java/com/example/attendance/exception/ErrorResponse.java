package com.example.attendance.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final String message;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;
    private String detail;


    public ErrorResponse(ErrorCode ErrorCode, LocalDateTime localDateTime) {
        this.message = ErrorCode.getMessage();
        this.httpStatus = ErrorCode.getStatus();
        this.timestamp = localDateTime;
    }

    public static ErrorResponse of(ErrorCode code, LocalDateTime localDateTime) {
        return new ErrorResponse(code, localDateTime);
    }

    public void setDetail(String detailMessage) {
        this.detail = detailMessage;
    }
}
