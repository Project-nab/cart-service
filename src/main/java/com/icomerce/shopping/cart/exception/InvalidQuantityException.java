package com.icomerce.shopping.cart.exception;

public class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String error) {
        super(error);
    }
}
