package com.carservice.model.service;

/**
 * Enum representing different types of car service and maintenance
 */
public enum ServiceType {
    OIL_CHANGE("Oil Change", 5000),
    TIRE_ROTATION("Tire Rotation", 10000),
    BRAKE_SERVICE("Brake Service", 20000),
    AIR_FILTER("Air Filter Replacement", 15000),
    TRANSMISSION_FLUID("Transmission Fluid Change", 50000),
    ENGINE_TUNE_UP("Engine Tune-Up", 30000),
    WHEEL_ALIGNMENT("Wheel Alignment", 15000),
    BATTERY_REPLACEMENT("Battery Replacement", 50000),
    FLUID_CHECK("Fluid Check", 5000),
    GENERAL_INSPECTION("General Inspection", 10000),
    OTHER("Other Service", 0);

    private final String description;
    private final int recommendedMileageInterval;

    ServiceType(String description, int recommendedMileageInterval) {
        this.description = description;
        this.recommendedMileageInterval = recommendedMileageInterval;
    }

    public String getDescription() {
        return description;
    }

    public int getRecommendedMileageInterval() {
        return recommendedMileageInterval;
    }

    /**
     * Get a ServiceType from a string description
     * @param description The service description
     * @return The matching ServiceType or OTHER if no match
     */
    public static ServiceType fromDescription(String description) {
        if (description == null) return OTHER;

        for (ServiceType type : ServiceType.values()) {
            if (type.getDescription().equalsIgnoreCase(description)) {
                return type;
            }
        }
        return OTHER;
    }

    @Override
    public String toString() {
        return description;
    }
}