package com.app.food.model;

public enum OrderStatus {
    CREATED,       // user placed order, waiting confirmation
    CONFIRMED,     // restaurant/admin confirmed
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}