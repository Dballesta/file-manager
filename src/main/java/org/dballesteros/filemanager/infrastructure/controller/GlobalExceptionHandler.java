package org.dballesteros.filemanager.infrastructure.controller;

import lombok.extern.slf4j.Slf4j;
import org.dballesteros.filemanager.domain.model.exception.ExceptionDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ProblemDetail> handleCustomException(final ExceptionDetail ex) {
        log.error("Custom exception occurred: Code - {}, Message - {}",
                ex.getCode(), ex.getMessage(), ex);

        return Mono.just(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ProblemDetail> handleNotHandledException(final Throwable ex) {
        log.error("Unexpected exception occurred: Message - {}", ex.getMessage(), ex);
        return Mono.just(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
