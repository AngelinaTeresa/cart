package com.shopping.cart.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopping.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {
    public String message;
    public String datetime;
    public String statusCode;
    public List<Cart> unavailableItems;
}
