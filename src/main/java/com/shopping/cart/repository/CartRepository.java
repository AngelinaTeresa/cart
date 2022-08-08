package com.shopping.cart.repository;

import com.shopping.cart.entity.Cart;
import com.shopping.cart.entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {

    List<Cart> findByCartIdUserId(int userId);

    Cart findByCartId(int cartId);

   @Query("SELECT c.totalProductPrice FROM Cart c WHERE c.cartId.userId = ?1")
   List<Integer> getCartPrice(int userId);
}
