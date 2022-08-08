package com.shopping.cart.service;

import com.shopping.cart.dao.CartDao;
import com.shopping.cart.exception.CartServiceException;
import com.shopping.cart.exception.CheckOutException;
import com.shopping.cart.exception.ProductServiceException;
import com.shopping.cart.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    CartDao cartDao;

    @Autowired
    ProductService productService;

    public void addToCart(Cart newCart){

        List<Cart> cartList = cartDao.getCart(newCart.getCartId().getUserId());
        Optional<Cart> cart = cartList.stream().filter
                        (item -> item.getCartId().getProductId() == newCart.getCartId().getProductId()).findFirst();
        int productPrice = newCart.getProductPrice() * newCart.getProductQuantity();
        if(cart.isPresent()) {
            Cart item = cart.get();
            item.setProductQuantity(item.getProductQuantity()+newCart.getProductQuantity());
            item.setTotalProductPrice(item.getTotalProductPrice()+ productPrice);
            addItemToCart(item);

        } else{
            newCart.setTotalProductPrice(productPrice);
            addItemToCart(newCart);
        }
    }

    private void addItemToCart(Cart item) {
        if(!productService.validProduct(item)){
            throw new ProductServiceException("Product is currently unavailable", HttpStatus.NOT_ACCEPTABLE);
        }
        cartDao.addToCart(item);
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
        item.setProductQuantity(item.getProductQuantity()-1);
        item.setTotalProductPrice(item.getTotalProductPrice()-item.getProductPrice());
        cartDao.addToCart(item);
    }

    public int getCartPrice(int userId) {
        int sum=0;
        List<Integer> cartList = cartDao.getCartPrice(userId);
        sum = cartList.stream().reduce(0,(a,b)->a+b);
       return sum;
    }

    public void checkoutCart(int userId) {
        List<Cart> items = getCart(userId);
        List<Cart> unavailableItems = new ArrayList<>();
        items.stream().forEach(item -> {
            if(!productService.validProduct(item)){
                unavailableItems.add(item);
            }
        });
        if(unavailableItems.isEmpty()){
            deleteCart(userId);
            //update product db
            productService.updateProductQuantity(items);
        }
        else {
            throw new CheckOutException("Few items are currently unavailable. Please review the cart", HttpStatus.INTERNAL_SERVER_ERROR,
                    unavailableItems);
        }
    }
}
