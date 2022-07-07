package com.shopping.cart.exception;

import com.shopping.cart.model.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CartServiceException.class)
    public ResponseEntity<Object> exception(CartServiceException exception) {
        Error error = new Error(exception.errorMessage, LocalDateTime.now().toString(),
                exception.httpStatus.toString());
        return new ResponseEntity<>(error, exception.httpStatus);
    }
}
