package org.parking_lot.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Floor {
    private final int floorNum;
    private final HashSet<Spot> allSpots = new HashSet<>();;

    private PriorityBlockingQueue<Spot> vacantReservedQueue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Spot> vacantCompactQueue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Spot> vacantRegularQueue = new PriorityBlockingQueue<>();
    private PriorityBlockingQueue<Spot> vacantLargeQueue = new PriorityBlockingQueue<>();

    Floor(int floorNum,
          HashSet<Spot> reservedSet,
          HashSet<Spot> compactSet,
          HashSet<Spot> regularSet,
          HashSet<Spot> largeSet
    ) {
        if(reservedSet == null || compactSet == null || largeSet == null || regularSet == null) {
            throw new IllegalArgumentException("Floor Constructor arguments cannot be null");
        }
        this.floorNum = floorNum;

        this.allSpots.addAll(reservedSet);
        this.allSpots.addAll(compactSet);
        this.allSpots.addAll(regularSet);
        this.allSpots.addAll(largeSet);

        this.vacantReservedQueue.addAll(reservedSet);
        this.vacantCompactQueue.addAll(compactSet);
        this.vacantRegularQueue.addAll(regularSet);
        this.vacantLargeQueue.addAll(largeSet);
    }

    public int getFloorNum() {
        return floorNum;
    }

    // Finding a spot should be immediately followed by attempting check-in
    // In case of multiple threads (multiple entrances) looking for a spot
    // it's possible that a vacant spot is shown to two different threads
    // then only one is able to take it, leaving the other thread throwing an error

    public Spot attemptCheckIn(Vehicle vehicle, LocalDateTime now) {
        Spot spot = null;

        for(SpotCategory category: vehicle.readCompatibleSpots()) {
            spot = switch(category) {
                case SpotCategory.RESERVED -> this.getSpot(vacantReservedQueue, now, vehicle);
                case SpotCategory.COMPACT -> this.getSpot(vacantCompactQueue, now, vehicle);
                case SpotCategory.REGULAR -> this.getSpot(vacantRegularQueue, now, vehicle);
                case SpotCategory.LARGE -> this.getSpot(vacantLargeQueue, now, vehicle);
            };
            if(spot != null) return spot;
        }
        throw new NoSpotFoundError("No spot available for the vehicle: " + vehicle.getLicensePlate());
    }

    private Spot getSpot(Queue<Spot> vacantQueue, LocalDateTime now, Vehicle vehicle) {
        if(vacantQueue.isEmpty()) return null;
        Spot spot = vacantQueue.poll();
        try {
            spot.checkInVehicle(vehicle.getLicensePlate(), now, vehicle.getVehicleClass());
        } catch(Exception e) {
            vacantQueue.offer(spot);
            return null;
        }
        return spot;
    }

    public double getBill(Spot spot) {
        return spot.getBill();
    }

    public boolean attemptCheckout(Spot spot, String vehicleNum) {
        try {
            spot.checkOutVehicle(vehicleNum);
        } catch (Exception e) {
            return false;
        }
        switch(spot.readCategory()) {
            case RESERVED -> this.vacantReservedQueue.offer(spot);
            case COMPACT -> this.vacantCompactQueue.offer(spot);
            case REGULAR -> this.vacantRegularQueue.offer(spot);
            case LARGE -> this.vacantLargeQueue.offer(spot);
        }
        return true;
    }
}



