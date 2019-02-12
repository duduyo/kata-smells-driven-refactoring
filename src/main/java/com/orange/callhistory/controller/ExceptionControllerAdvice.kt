package com.orange.callhistory.controller

import com.orange.callhistory.controller.dto.ErrorDto

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CallException::class)
    @ResponseBody
    internal fun handleCallException(ex: RuntimeException): ErrorDto {
        val errorDto = ErrorDto()
        errorDto.name = "Call exception"
        errorDto.message = ex.message
        return errorDto
    }
}
