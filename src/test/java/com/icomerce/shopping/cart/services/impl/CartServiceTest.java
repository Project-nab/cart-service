package com.icomerce.shopping.cart.services.impl;

import com.icomerce.shopping.cart.entitties.Cart;
import com.icomerce.shopping.cart.exception.InvalidQuantityException;
import com.icomerce.shopping.cart.exception.ProductCodeNotFoundException;
import com.icomerce.shopping.cart.exception.QuantityOverException;
import com.icomerce.shopping.cart.repositories.CartItemRepo;
import com.icomerce.shopping.cart.repositories.CartRepo;
import com.icomerce.shopping.cart.services.CartService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CartServiceTest {
    @Autowired
    public CartRepo cartRepo;

    @Autowired
    public CartItemRepo cartItemRepo;

    @Autowired
    public CartService cartService;

    @After
    public void tearDown() {
        cartItemRepo.deleteAll();
        cartRepo.deleteAll();
    }

    @Test
    public void whenCreateCard_thenReturnCart() throws InvalidQuantityException, ProductCodeNotFoundException,
            QuantityOverException {
        // When
        cartService.addCart("baonc93@gmail.com", "4EF9C11DD7E95AEA3505D0BF17F23DAC",
                "ADIDAS_TSHIRT_01", 1);
        // Then
        assertEquals(1, cartRepo.count());
    }

    @Test(expected = QuantityOverException.class)
    public void whenCreateCartQuantityGreaterThanProductQuantity_thenThrowException() throws InvalidQuantityException,
            ProductCodeNotFoundException, QuantityOverException {
        cartService.addCart("baonc93@gmail.com", "4EF9C11DD7E95AEA3505D0BF17F23DAC",
                "ADIDAS_TSHIRT_01", 20000);
    }

    @Test(expected = InvalidQuantityException.class)
    public void whenCreateCartQuantityLessThanOrEqualZero_thenThrowException() throws InvalidQuantityException,
            ProductCodeNotFoundException, QuantityOverException {
        cartService.addCart("baonc93@gmail.com", "4EF9C11DD7E95AEA3505D0BF17F23DAC",
                "ADIDAS_TSHIRT_01", 0);
    }

    @Test(expected = ProductCodeNotFoundException.class)
    public void whenCreateCartWrongProductCode_thenThrowException() throws InvalidQuantityException,
            ProductCodeNotFoundException, QuantityOverException {
        cartService.addCart("baonc93@gmail.com", "4EF9C11DD7E95AEA3505D0BF17F23DAC",
                "WRONG_PRODUCT_CODE", 0);
    }
}
