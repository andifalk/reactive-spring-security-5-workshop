package com.example.library.server.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public Mono<ResponseEntity<String>> handle(AccessDeniedException ex) {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.error(ex.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
  }

  @ExceptionHandler(RuntimeException.class)
  public Mono<ResponseEntity<String>> handle(RuntimeException ex) {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.error(ex.getMessage());
    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<String>> handle(Exception ex) {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.error(ex.getMessage());
    return Mono.just(ResponseEntity.badRequest().build());
  }
}
