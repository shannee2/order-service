package com.order.exceptions.order;

public class InvalidRestaurantIdException extends IllegalArgumentException{
    public InvalidRestaurantIdException(String message) {
        super(message);
    }
}
