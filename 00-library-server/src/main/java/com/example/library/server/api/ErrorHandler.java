package com.example.library.server.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public Mono<ResponseEntity<String>> handle(RuntimeException ex) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @ExceptionHandler
    public Mono<ResponseEntity<String>> handle(Exception ex) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error(ex.getMessage());
        return Mono.just(ResponseEntity.badRequest().build());
    }

}
