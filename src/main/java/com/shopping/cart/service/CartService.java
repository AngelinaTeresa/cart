package com.shopping.cart.service;

import com.shopping.cart.dao.CartDao;
import com.shopping.cart.exception.CartServiceException;
import com.shopping.cart.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartDao cartDao;

    public void addToCart(Cart newCart){
        //product api call check inventory, throw exception if needed

        List<Cart> cartList = cartDao.getCart(newCart.getUserId());
        Optional<Cart> cart = cartList.stream().filter
                        (item -> item.getProductName().equalsIgnoreCase(newCart.getProductName())).findFirst();
        if(cart.isPresent()) {
            Cart item = cart.get();
            item.setProductQuantity(item.getProductQuantity()+1);
            item.setTotalProductPrice(item.getTotalProductPrice()+newCart.getTotalProductPrice());
            cartDao.addToCart(item);
        } else{
            cartDao.addToCart(newCart);
        }
    }
    public List<Cart> getCart(int userId){
       return cartDao.getCart(userId);
    }

    public Cart getCartItem(int cartId) throws CartServiceException {
        Cart cart = cartDao.getCartItem(cartId);
        if(Objects.isNull(cart)) {
            throw new CartServiceException("Selected item is not present in the cart", HttpStatus.BAD_REQUEST);
        }
        return cart;
    }

    public void deleteItem(int cartId) throws CartServiceException {cartDao.deleteItem(getCartItem(cartId));}

    public void deleteCart(int userId) {
        List<Cart> items = getCart(userId);
        cartDao.deleteCart(items);
    }

    public void decreaseItem(int cartId) {
        Cart item = cartDao.getCartItem(cartId);
        int productPrice = item.getTotalProductPrice()/item.getProductQuantity();
        item.setProductQuantity(item.getProductQuantity()-1);
        item.setTotalProductPrice(item.getTotalProductPrice()-productPrice);
        cartDao.addToCart(item);
    }

    public int getCartPrice(int userId) {
        int sum=0;
        List<Integer> cartList = cartDao.getCartPrice(userId);
        sum = cartList.stream().reduce(0,(a,b)->a+b);
       return sum;
    }

    public void checkoutCart(int userId) {
        //check product availability
        deleteCart(userId);
        //update product db
    }
}
