package com.robbin.order_service.circuitbreaker;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InventoryCircuitBreaker {

    private volatile CircuitState state =
            CircuitState.CLOSED;

    private final AtomicInteger failures =
            new AtomicInteger(0);

    private volatile long openTimestamp;

    private static final int FAILURE_THRESHOLD = 3;

    private static final long OPEN_DURATION =
            10000;

    public boolean allowRequest() {

        if(state == CircuitState.OPEN) {

            long elapsed =
                    System.currentTimeMillis()
                            - openTimestamp;

            if(elapsed > OPEN_DURATION) {

                state =
                        CircuitState.HALF_OPEN;

                return true;
            }

            return false;
        }

        return true;
    }

    public void recordSuccess() {

        failures.set(0);

        state =
                CircuitState.CLOSED;
    }

    public void recordFailure() {

        int count =
                failures.incrementAndGet();

        if(count >= FAILURE_THRESHOLD) {

            state =
                    CircuitState.OPEN;

            openTimestamp =
                    System.currentTimeMillis();

            System.out.println(
                    "Circuit OPEN"
            );
        }
    }

    public CircuitState getState() {

        return state;
    }
}