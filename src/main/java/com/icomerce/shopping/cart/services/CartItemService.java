package com.icomerce.shopping.cart.services;

import com.icomerce.shopping.cart.entitties.CartItem;
import org.springframework.data.domain.Page;

public interface CartItemService {
    Page<CartItem> findAllByCartSessionId(String sessionId, int offset, int limit);
}
