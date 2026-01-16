package org.parking_lot.core;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

public class Vehicle {
    private final String licensePlate;
    private final VehicleClass vehicleClass;
    private EnumSet<SpotCategory> compatibleSpots = EnumSet.noneOf(SpotCategory.class);
    private static final Map<VehicleClass, EnumSet<SpotCategory>> COMPATIBILITY_CACHE = new EnumMap<>(VehicleClass.class);

    static {
        for(VehicleClass vc : VehicleClass.values()) {
            EnumSet<SpotCategory> compatibleSpots = EnumSet.noneOf(SpotCategory.class);
            for (SpotCategory category: SpotCategory.values()) {
                if(category.getAllowedVehicles().contains(vc)) compatibleSpots.add(category);
            }
            COMPATIBILITY_CACHE.put(vc, compatibleSpots);
        }
    }

    Vehicle(String licensePlate, VehicleClass vehicleClass) {
        this.licensePlate = licensePlate;
        this.vehicleClass = vehicleClass;
        this.compatibleSpots = COMPATIBILITY_CACHE.get(vehicleClass);
    }

    public VehicleClass getVehicleClass() {return this.vehicleClass;}
    public String getLicensePlate() {return this.licensePlate;}
    public EnumSet<SpotCategory> readCompatibleSpots() {return this.compatibleSpots;}
}
