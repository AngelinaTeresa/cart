package com.shopping.cart.exception;

import com.shopping.cart.model.Cart;
import org.springframework.http.HttpStatus;

import java.util.List;

public class CheckOutException  extends RuntimeException {
    String errorMessage;
    HttpStatus httpStatus;

    List<Cart> unavailableItems;

    public CheckOutException(String errorMessage, HttpStatus httpStatus, List<Cart> unavailableItems){
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
        this.unavailableItems = unavailableItems;
    }
}
