package com.icomerce.shopping.cart.services.imp;

import com.icomerce.shopping.cart.entitties.CartItem;
import com.icomerce.shopping.cart.repositories.CartItemRepo;
import com.icomerce.shopping.cart.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImp implements CartItemService {
    private final CartItemRepo cartItemRepo;

    @Autowired
    public CartItemServiceImp(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    @Cacheable(value = "items")
    public Page<CartItem> findAllByCartSessionId(String sessionId, int offset, int limit) {
        Page<CartItem> cartItems = cartItemRepo.findAllByCartSessionId(sessionId, PageRequest.of(offset, limit));
        return cartItems;
    }
}
