package com.example.attendance.exception;

public class DuplicateException extends CustomException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
