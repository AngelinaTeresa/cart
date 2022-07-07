package com.shopping.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    public String message;
    public String datetime;
    public String statusCode;
}
