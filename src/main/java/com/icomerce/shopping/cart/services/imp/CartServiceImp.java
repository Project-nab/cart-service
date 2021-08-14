package com.icomerce.shopping.cart.services.imp;

import com.icomerce.shopping.cart.clients.ProductClient;
import com.icomerce.shopping.cart.entitties.Cart;
import com.icomerce.shopping.cart.entitties.CartItem;
import com.icomerce.shopping.cart.entitties.CartStatus;
import com.icomerce.shopping.cart.exception.InvalidCartSessionIdException;
import com.icomerce.shopping.cart.exception.InvalidQuantityException;
import com.icomerce.shopping.cart.exception.ProductCodeNotFoundException;
import com.icomerce.shopping.cart.exception.QuantityOverException;
import com.icomerce.shopping.cart.payload.response.ProductClientResponse;
import com.icomerce.shopping.cart.repositories.CartItemRepo;
import com.icomerce.shopping.cart.repositories.CartRepo;
import com.icomerce.shopping.cart.services.CartService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImp implements CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;

    private final ProductClient productClient;

    @Autowired
    public CartServiceImp(CartRepo cartRepo, CartItemRepo cartItemRepo, ProductClient productClient) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.productClient = productClient;
    }

    @Override
    public Cart addCart(String username, String sessionId, String productCode, int quantity) throws QuantityOverException,
            ProductCodeNotFoundException, InvalidQuantityException {
        ProductClientResponse productClientResponse;

        try {
            productClientResponse = productClient.getProductByCode(productCode);
        } catch (FeignException.BadRequest e) {
            log.error("Bad request exception invalid product id");
            throw new ProductCodeNotFoundException("Product code not found " + productCode);
        }

        if(productClientResponse.getErrorCode() == HttpServletResponse.SC_BAD_REQUEST) {
            throw new ProductCodeNotFoundException("Product code not found " + productCode);
        }

        if(productClientResponse.getResult().getQuantity() < quantity) {
            throw new QuantityOverException("Don't have enough quantity. Product code " + productCode);
        }

        if(quantity <= 0) {
            throw new InvalidQuantityException("Invalid quantity, quantity have to greater than 0");
        }

        Optional<Cart> cart = cartRepo.findBySessionId(sessionId);
        Cart createdCart = cart.orElseGet(() -> new Cart(sessionId, username, Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now()), CartStatus.NEW, null));
        CartItem cartItem = new CartItem();
        cartItem.setCart(createdCart);
        cartItem.setProductCode(productCode);
        cartItem.setQuantity(quantity);
        cartItem.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        cartItem.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        cartRepo.save(createdCart);
        cartItemRepo.save(cartItem);
        return createdCart;
    }

    @Override
    @Cacheable(value = "carts")
    public Page<Cart> getAll(String username, int offset, int limit) {
        return cartRepo.findAllByUsername(username, PageRequest.of(offset, limit));
    }

    @Override
    public Cart getCartBySessionId(String sessionId) throws InvalidCartSessionIdException {
        Optional<Cart> cart = cartRepo.findBySessionId(sessionId);
        if(cart.isPresent()) {
            return cart.get();
        }
        throw new InvalidCartSessionIdException("Invalid cart session id " + sessionId);
    }

    @Override
    public Cart updateStatus(String sessionId, CartStatus cartStatus) throws InvalidCartSessionIdException {
        Optional<Cart> cart = cartRepo.findBySessionId(sessionId);
        if(cart.isPresent()) {
            cart.get().setCartStatus(cartStatus);
            cartRepo.save(cart.get());
            return cart.get();
        }
        throw new InvalidCartSessionIdException("Invalid cart session id " + sessionId);
    }
}
