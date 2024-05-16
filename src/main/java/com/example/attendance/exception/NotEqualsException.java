package com.example.attendance.exception;

public class NotEqualsException extends CustomException {

    public NotEqualsException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotEqualsException(ErrorCode errorCode, String detailMessage) {
        super(errorCode, detailMessage);
    }
}
