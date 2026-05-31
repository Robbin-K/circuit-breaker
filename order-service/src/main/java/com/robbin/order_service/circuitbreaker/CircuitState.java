package com.robbin.order_service.circuitbreaker;

public enum CircuitState {

    CLOSED,
    OPEN,
    HALF_OPEN
}
