package com.icomerce.shopping.cart.controllers;

import com.icomerce.shopping.cart.entitties.Cart;
import com.icomerce.shopping.cart.exception.InvalidCartSessionIdException;
import com.icomerce.shopping.cart.exception.InvalidQuantityException;
import com.icomerce.shopping.cart.exception.ProductCodeNotFoundException;
import com.icomerce.shopping.cart.exception.QuantityOverException;
import com.icomerce.shopping.cart.payload.request.CreateCartRequest;
import com.icomerce.shopping.cart.payload.response.BaseResponse;
import com.icomerce.shopping.cart.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@Slf4j
public class CartController {
    private final CartService cartService;
    private final HttpServletResponse response;

    @Autowired
    public CartController(CartService cartService, HttpServletResponse response) {
        this.cartService = cartService;
        this.response = response;
    }

    @RequestMapping(value = "/carts", method = RequestMethod.POST)
    public BaseResponse createCart(@RequestBody CreateCartRequest request,
                                   HttpSession session,
                                   Principal principal) {
        String username = principal.getName();
        String sessionId = session.getId();
        BaseResponse responseBody;
        try {
            Cart cart = cartService.addCart(username, sessionId, request.getProductCode(), request.getQuantity());
            responseBody = new BaseResponse(HttpServletResponse.SC_CREATED, "Add cart successful", cart);
        } catch (QuantityOverException e) {
            log.error("Quantity over exception ", e);
            responseBody = new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, "Quantity is over", null);
        } catch (ProductCodeNotFoundException e) {
            log.error("Product code not found exception ", e);
            responseBody = new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, "Invalid product code", null);
        } catch (InvalidQuantityException e) {
            log.error("Invalid quantity exception ", e);
            responseBody = new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, "Quantity have to greater than 0", null);
        }
        return responseBody;
    }

    @RequestMapping(value = "/carts", method = RequestMethod.GET)
    public BaseResponse findAllByUsername(@RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
                                          @RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
                                          Principal principal) {
        String username = principal.getName();
        Page<Cart> carts = cartService.getAll(username, offset, limit);
        return new BaseResponse(HttpServletResponse.SC_OK, "Success", carts);
    }

    @RequestMapping(value = "/carts/{sessionId}", method = RequestMethod.GET)
    public BaseResponse findByCartSessionId(@PathVariable(value = "sessionId") String sessionId) {
        Cart cart;
        try {
            cart = cartService.getCartBySessionId(sessionId);
            return new BaseResponse(HttpServletResponse.SC_OK, "Success", cart);
        } catch (InvalidCartSessionIdException invalidCartSessionIdException) {
            log.error("Invalid cart session id");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, invalidCartSessionIdException.getMessage(), null);
        }
    }

    @RequestMapping(value = "/carts/{sessionId}", method = RequestMethod.PUT)
    public BaseResponse updateCartStatus(@RequestBody CreateCartRequest request,
                                         @PathVariable(value = "sessionId") String sessionId) {
        try {
            Cart cart = cartService.updateStatus(sessionId, request.getCartStatus());
            return new BaseResponse(HttpServletResponse.SC_OK, "Success", cart);
        } catch (InvalidCartSessionIdException e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new BaseResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        }
    }
}
