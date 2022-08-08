package com.shopping.cart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Cart")
public class Cart {

    @EmbeddedId
    CartId cartId;
    String productName;
    int productQuantity;
    int productPrice;
    int totalProductPrice;
}
