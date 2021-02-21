package com.seesawin.controller.handler;

import com.seesawin.payload.response.CommonResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CommonResponse handleException(Exception ex, HttpServletResponse response) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode("01");
        commonResponse.setMsg(ex.getMessage());
        response.setStatus(HttpStatus.OK.value());
        return commonResponse;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errMsgs = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ":" + fieldError.getDefaultMessage())
                .collect(Collectors.joining(";"));
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode("01");
        commonResponse.setMsg(errMsgs);
        return handleExceptionInternal(ex, commonResponse, headers, HttpStatus.OK, request);
    }
}
