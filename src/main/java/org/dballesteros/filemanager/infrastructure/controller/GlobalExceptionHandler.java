package org.dballesteros.filemanager.infrastructure.controller;

import lombok.extern.slf4j.Slf4j;
import org.dballesteros.filemanager.domain.model.ExceptionDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionDetail.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleCustomException(final ExceptionDetail ex) {
        log.error("Custom exception occurred: Code - {}, Message - {}", ex.getCode(), ex.getMessage());
    }
}
