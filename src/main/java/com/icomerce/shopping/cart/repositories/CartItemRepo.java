package com.icomerce.shopping.cart.repositories;

import com.icomerce.shopping.cart.entitties.Cart;
import com.icomerce.shopping.cart.entitties.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartItemRepo extends CrudRepository<CartItem, Long> {
    Page<CartItem> findAllByCartSessionId(String sessionId, Pageable pageable);
    void deleteByCartSessionIdAndProductCode(String sessionId, String productCode);
    Optional<CartItem> findAllByCartSessionIdAndProductCode(String sessionId, String productCode);
}
