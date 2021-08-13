package com.icomerce.shopping.cart.repositories;

import com.icomerce.shopping.cart.entitties.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepo extends CrudRepository<Cart, String> {
    Optional<Cart> findBySessionId(String sessionId);
    Page<Cart> findAllByUsername(String username, Pageable pageable);
}
