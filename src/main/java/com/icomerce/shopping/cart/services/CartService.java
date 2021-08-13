package com.icomerce.shopping.cart.services;

import com.icomerce.shopping.cart.entitties.Cart;
import com.icomerce.shopping.cart.exception.InvalidQuantityException;
import com.icomerce.shopping.cart.exception.ProductCodeNotFoundException;
import com.icomerce.shopping.cart.exception.QuantityOverException;
import org.springframework.data.domain.Page;

public interface CartService {
    Cart addCart(String username, String sessionId, String productCode, int quantity) throws
            QuantityOverException, ProductCodeNotFoundException, InvalidQuantityException;
    Page<Cart> getAll(String username, int offset, int limit);
}
