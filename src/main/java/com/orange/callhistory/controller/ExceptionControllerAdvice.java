package com.orange.callhistory.controller;

import com.orange.callhistory.controller.dto.ErrorDto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CallException.class)
    @ResponseBody
    ErrorDto handleCallException(RuntimeException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setName("Call exception");
        errorDto.setMessage(ex.getMessage());
        return errorDto;
    }
}
