package com.shopping.cart.controller;

import com.shopping.cart.exception.CartServiceException;
import com.shopping.cart.exception.ProductServiceException;
import com.shopping.cart.model.Cart;
import com.shopping.cart.service.CartService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final String CART_SERVICE = "cartService";

    @Autowired
    CartService cartService;

    @GetMapping("/getCart/{userId}")
    public ResponseEntity getCart(@PathVariable int userId){
        List<Cart> cart = cartService.getCart(userId);
        return new ResponseEntity(cart, HttpStatus.OK);
    }

    @PostMapping("/addItem")
    @CircuitBreaker(name = CART_SERVICE )
    @Retry(name = CART_SERVICE )
    @RateLimiter(name = CART_SERVICE)
    public ResponseEntity addToCart(@RequestBody Cart cart){
        cartService.addToCart(cart);
    return new ResponseEntity("Added item to Cart",HttpStatus.OK);}

    @DeleteMapping("/deleteItem/{cartId}")
    public ResponseEntity deleteItem(@PathVariable int cartId) throws CartServiceException {
         cartService.deleteItem(cartId);
        return new ResponseEntity("Deleted item from the cart",HttpStatus.OK);
    }

    @PutMapping("/decreaseItem/{cartId}")
    public ResponseEntity decreaseItem(@PathVariable int cartId){
        cartService.decreaseItem(cartId);
        return new ResponseEntity("Reduced quantity by one",HttpStatus.OK);
    }

    @DeleteMapping("/deleteCart/{userId}")
    public ResponseEntity deleteCart(@PathVariable int userId){
        cartService.deleteCart(userId);
        return new ResponseEntity("Deleted Cart",HttpStatus.OK);
    }

    @GetMapping("/getCartPrice/{userId}")
    public ResponseEntity getCartPrice(@PathVariable int userId){
        int cartPrice = cartService.getCartPrice(userId);
        return new ResponseEntity(cartPrice, HttpStatus.OK);
    }

    @DeleteMapping("/checkout/{userId}")
    public ResponseEntity checkout(@PathVariable int userId){
        cartService.checkoutCart(userId);
        return new ResponseEntity("Checkout cart completed",HttpStatus.OK);
    }

    public ResponseEntity<Object> fallBackForAddItem(Cart cart, ProductServiceException ex ){

        return new ResponseEntity<Object>("Product Service Down",HttpStatus.BAD_REQUEST);
    }
}
