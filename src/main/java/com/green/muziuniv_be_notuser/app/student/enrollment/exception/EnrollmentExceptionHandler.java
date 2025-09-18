package com.green.muziuniv_be_notuser.app.student.enrollment.exception;

import com.green.muziuniv_be_notuser.app.student.enrollment.EnrollmentController;
import com.green.muziuniv_be_notuser.configuration.model.ResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {EnrollmentController.class})
public class EnrollmentExceptionHandler {
    //EnrollmentException 터지면 여기서 잡힘.
    @ExceptionHandler(EnrollmentException.class)
    public ResponseEntity<ResultResponse<Void>> handleEnrollmentException(EnrollmentException e) {
        ResultResponse<Void> errorRes =
                new ResultResponse<>(e.getMessage(), null);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorRes);
    }


}
