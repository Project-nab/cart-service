package com.icomerce.shopping.cart.exception;

public class InvalidCartSessionIdException extends Exception {
    public InvalidCartSessionIdException(String error) {
        super(error);
    }
}
