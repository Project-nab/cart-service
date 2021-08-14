package com.icomerce.shopping.cart.payload.request;

import com.icomerce.shopping.cart.entitties.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartRequest {
    private String productCode;
    private int quantity = 0;
    private CartStatus cartStatus;
}
