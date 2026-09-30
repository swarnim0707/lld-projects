package org.vending_machine.core;

public enum ItemCategory {
    LAYS("Lays", 20.0),
    KURKURE("Kurkure", 20.0),
    CHOCLAIR("Choclair", 10.0),
    MILKSHAKE("Milkshake", 30);

    private final String name;
    private final double cost;

    ItemCategory(String name, double cost) {
        if(name == null || name.isEmpty()) throw new IllegalArgumentException("name cannot be null or empty");
        this.name = name;
        this.cost = cost;
    }
}