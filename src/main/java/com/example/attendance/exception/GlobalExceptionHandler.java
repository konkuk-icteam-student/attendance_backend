package com.example.attendance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 아래와 같은 형식의 예외를 프론트에 던져줍니다
     * {
     * "message": "해당 부서를 찾을 수 없습니다.",
     * "httpStatus": "NOT_FOUND",
     * "timestamp": "2024-02-28T15:28:59.140106",
     * "detail": "해당 Id의 부서가 존재하지 않습니다."
     * }
     */
    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, LocalDateTime.now());
        errorResponse.setDetail(e.getMessage());
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnKnownException(Exception ex) {
        String errorMessage = ex.getMessage();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }


//    @ExceptionHandler
//    protected ResponseEntity<String> handleRuntimeException(RuntimeException e) {
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }

}

