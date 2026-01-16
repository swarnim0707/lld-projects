package org.parking_lot.core;

public class NoSpotFoundError extends RuntimeException {
    public NoSpotFoundError(String message) {
        super(message);
    }
}
