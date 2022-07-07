package com.shopping.cart.exception;


import org.springframework.http.HttpStatus;

public class CartServiceException extends Exception {
    String errorMessage;
    HttpStatus httpStatus;

    public CartServiceException(String errorMessage, HttpStatus httpStatus){
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
