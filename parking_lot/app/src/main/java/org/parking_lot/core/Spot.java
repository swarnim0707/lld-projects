package org.parking_lot.core;

import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.time.Duration;

public class Spot implements Comparable<Spot> {
    private final int distanceFromEntrance;
    private final  SpotCategory category;
    private boolean vacant = false;
    private final String label;
    private LocalDateTime checkinTime;
    private String vehicleNum = "";
    private double bill = 0.0;
    private final int floor;

    Spot(int distanceFromEntrance, SpotCategory category, String label, int floor) {
        this.distanceFromEntrance = distanceFromEntrance;
        this.category = category;
        this.label = label;
        this.floor = floor;
    }

    @Override
    public int compareTo(@NonNull Spot other) {
        // Prioritize closer spots
        return Integer.compare(this.distanceFromEntrance, other.distanceFromEntrance);
    }

    public boolean readVacancy() { return this.vacant; }

    public String readVehicleNum() { return this.vehicleNum; }

    public String readLabel() { return this.label; }

    public SpotCategory readCategory() { return this.category; }

    public double getBill() {
        if(this.vacant) {
            return this.bill;
        }
        this.bill = this.category.getRate() * (Duration.between(this.checkinTime, LocalDateTime.now())).toHours();
        return this.bill;
    }

    public void checkOutVehicle(String vehicleNum) {
        if(this.vacant) {
            throw new IllegalStateException("Cannot check out vehicle when the spot is already empty");
        }
        else if(!vehicleNum.equals(this.vehicleNum)) {
            throw new IllegalArgumentException("Wrong vehicle num");
        }
        this.vacant = true;
        this.bill = 0.0;
        this.checkinTime = null;
        this.vehicleNum = "";
    }

    public LocalDateTime readCheckInTime() {
        if(this.checkinTime != null) { return this.checkinTime; }
        throw new NullPointerException("checkinTime is null");
    }

    public synchronized void checkInVehicle(String vehicleNum, LocalDateTime now, VehicleClass vehicleClass) {
        if(!this.vacant) throw new IllegalStateException("Cannot check in a vehicle when the spot is not vacant");
        if(!this.category.getAllowedVehicles().contains(vehicleClass)) {
            throw new IllegalStateException("This spot cannot be taken by a " + vehicleClass.name());
        }
        this.checkinTime = now;
        this.vacant = false;
        this.vehicleNum = vehicleNum;
    }
}
