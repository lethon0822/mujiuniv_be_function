package com.green.muziuniv_be_notuser.student.enrollment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class EnrollmentExceptionHandler {
    //EnrollmentException 터지면 여기서 잡힘.
    @ExceptionHandler(EnrollmentException.class)
    public ResponseEntity<String> handleEnrollmentException(EnrollmentException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    //서버에러 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("수강신청 실패! 서버 오류가 발생했습니다.");
    }
}
