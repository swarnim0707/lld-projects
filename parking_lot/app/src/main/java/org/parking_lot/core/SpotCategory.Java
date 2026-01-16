package org.parking_lot.core;

import java.util.*;

public enum SpotCategory {
    RESERVED(new ArrayList<VehicleClass>(List.of(VehicleClass.MEDICAL)), 0),
    COMPACT(new ArrayList<VehicleClass>(List.of(VehicleClass.MOTORCYCLE)), 5),
    REGULAR(new ArrayList<VehicleClass>(List.of(VehicleClass.MOTORCYCLE, VehicleClass.CAR, VehicleClass.MEDICAL)), 10),
    LARGE(new ArrayList<VehicleClass>(List.of(VehicleClass.MEDICAL, VehicleClass.CAR, VehicleClass.TRUCK, VehicleClass.MOTORCYCLE)), 20);

    private EnumSet<VehicleClass> allowedVehicles = EnumSet.noneOf(VehicleClass.class);
    private final double rate;

    SpotCategory(List<VehicleClass> allowedVehicles, double rate) {
        if(allowedVehicles == null) { throw new IllegalArgumentException("Null values not allowed"); }
        this.allowedVehicles.addAll(allowedVehicles);
        this.rate = rate;
    }

    public EnumSet<VehicleClass> getAllowedVehicles() {
        return this.allowedVehicles;
    }

    public double getRate() {
        return this.rate;
    }
}
