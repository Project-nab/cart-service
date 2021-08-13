package com.icomerce.shopping.cart.controllers;

import com.icomerce.shopping.cart.entitties.CartItem;
import com.icomerce.shopping.cart.payload.response.BaseResponse;
import com.icomerce.shopping.cart.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class CartItemController {
    private final CartItemService cartItemService;

    @Autowired
    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @RequestMapping(value = "/carts/{cartSessionId}/items", method = RequestMethod.GET)
    public BaseResponse getCartItem(@PathVariable(value = "cartSessionId") String sessionId,
                                    @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                    @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        Page<CartItem> cartItems = cartItemService.findAllByCartSessionId(sessionId, offset, limit);
        return new BaseResponse(HttpServletResponse.SC_OK, "Success", cartItems);
    }
}
