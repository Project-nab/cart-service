package com.icomerce.shopping.cart.services.impl;

import com.icomerce.shopping.cart.entitties.CartItem;
import com.icomerce.shopping.cart.exception.InvalidQuantityException;
import com.icomerce.shopping.cart.exception.ProductCodeNotFoundException;
import com.icomerce.shopping.cart.exception.QuantityOverException;
import com.icomerce.shopping.cart.services.CartItemService;
import com.icomerce.shopping.cart.services.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CartItemServiceTest {
    @Autowired
    public CartService cartService;
    @Autowired
    public CartItemService cartItemService;

    @Test
    public void whenAddCard_thenReturnItem() throws InvalidQuantityException,
            ProductCodeNotFoundException, QuantityOverException {
        // When
        cartService.addCart("baonc93@gmail.com", "4EF9C11DD7E95AEA3505D0BF17F23DAC",
                "ADIDAS_TSHIRT_01", 1);
        Page<CartItem> cartItems = cartItemService.findAllByCartSessionId("4EF9C11DD7E95AEA3505D0BF17F23DAC",
                0, 10);
        // Then
        assertEquals(1, cartItems.getTotalElements());
    }
}
