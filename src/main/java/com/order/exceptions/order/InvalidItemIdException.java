package com.order.exceptions.order;

public class InvalidItemIdException extends IllegalArgumentException {
    public InvalidItemIdException(String message) {
        super(message);
    }
}