package com.projectx.microservice.productservice.controller;

import com.projectx.microservice.payloads.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseDto<String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return new ResponseEntity<>(new ResponseDto<>(null,"File too large!"),HttpStatus.EXPECTATION_FAILED);
    }
}
