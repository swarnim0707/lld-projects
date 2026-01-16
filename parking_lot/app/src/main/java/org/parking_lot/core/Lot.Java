package org.parking_lot.core;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Lot {
    private final List<Floor> floorList = new ArrayList<>();
    private Map<String, AbstractMap.SimpleEntry<Floor, Spot>> spotMap = new ConcurrentHashMap<>();
    private final Set<VehicleClass> allowedVehicleTypes = EnumSet.noneOf(VehicleClass.class);

    Lot(List<Floor> floorList, Set<VehicleClass> allowedVehicleTypes) {
        this.floorList.addAll(floorList);
        this.allowedVehicleTypes.addAll(allowedVehicleTypes);
    }

    public String attemptVehicleCheckIn(Vehicle vehicle, LocalDateTime now) {
        if(!allowedVehicleTypes.contains(vehicle.getVehicleClass())) return "This vehicle type is not allowed";
        Spot spot;
        for(Floor floor: this.floorList) {
            try {
                spot = floor.attemptCheckIn(vehicle, now);
            } catch(NoSpotFoundError e) {
                continue;
            }
                if(spot != null) {
                    AbstractMap.SimpleEntry<Floor, Spot> floorSpot = new AbstractMap.SimpleEntry<>(floor, spot);
                    spotMap.put(vehicle.getLicensePlate(), floorSpot);
                    return "Floor: " + floor.getFloorNum() + "; Spot: " + spot.readLabel();
            }
        }

        return "No spot available for the vehicle";
    }

    public String getBill(String vehicleNum) {
        AbstractMap.SimpleEntry<Floor, Spot> floorSpot = spotMap.get(vehicleNum);
        double bill = 0.0;
        if(floorSpot != null) {bill = floorSpot.getKey().getBill(floorSpot.getValue());}
        if(bill == 0.0) {
            return "Vehicle has not been checked-in yet";
        }
        return (String.valueOf(bill));
    }

    // It's better to check out using vehicle num over Vehicle object, otherwise the component
    // calling this method would need to maintain a list of the Vehicle objects, increasing memory load
    public String attemptCheckOut(String vehicleNum, boolean paymentCompleted) {
        if(!paymentCompleted) {
            return "Please complete the payment";
        }
        AbstractMap.SimpleEntry<Floor, Spot> floorSpot = spotMap.get(vehicleNum);
        boolean checkOutStatus = false;
        if(floorSpot != null) {
            checkOutStatus = floorSpot.getKey().attemptCheckout(floorSpot.getValue(), vehicleNum);
        }
        if(!checkOutStatus) return "This vehicle is not checked in";
        return "Checked out successfully";
    }
}