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
                exception.httpStatus.toString(), null);
        return new ResponseEntity<>(error, exception.httpStatus);
    }
    @ExceptionHandler(value = ProductServiceException.class)
    public ResponseEntity<Object> exception(ProductServiceException exception) {
        Error error = new Error(exception.errorMessage, LocalDateTime.now().toString(),
                exception.httpStatus.toString(), null);
        return new ResponseEntity<>(error, exception.httpStatus);
    }

    @ExceptionHandler(value = CheckOutException.class)
    public ResponseEntity<Object> exception(CheckOutException exception) {
        Error error = new Error(exception.errorMessage, LocalDateTime.now().toString(),
                exception.httpStatus.toString(), exception.unavailableItems);
        return new ResponseEntity<>(error, exception.httpStatus);
    }
}
