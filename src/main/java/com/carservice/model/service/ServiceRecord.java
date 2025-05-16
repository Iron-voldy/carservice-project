package com.carservice.model.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * ServiceRecord class representing a service or maintenance record for a car
 */
public class ServiceRecord {
    private String recordId;
    private String carId;
    private String userId;
    private ServiceType serviceType;
    private Date serviceDate;
    private Date nextServiceDate;
    private double mileage;
    private String description;
    private double cost;
    private boolean completed;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Constructor with all fields
    public ServiceRecord(String recordId, String carId, String userId, ServiceType serviceType,
                         Date serviceDate, Date nextServiceDate, double mileage,
                         String description, double cost, boolean completed) {
        this.recordId = recordId;
        this.carId = carId;
        this.userId = userId;
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.nextServiceDate = nextServiceDate;
        this.mileage = mileage;
        this.description = description;
        this.cost = cost;
        this.completed = completed;
    }

    // Default constructor
    public ServiceRecord() {
        this.recordId = UUID.randomUUID().toString();
        this.carId = "";
        this.userId = "";
        this.serviceType = ServiceType.OTHER;
        this.serviceDate = new Date();
        this.nextServiceDate = new Date(System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000)); // 90 days later
        this.mileage = 0.0;
        this.description = "";
        this.cost = 0.0;
        this.completed = false;
    }

    // Getters and setters
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Date getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(Date nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Calculate next service date based on the service type and current mileage
    public void calculateNextServiceDate(double averageMilesPerMonth) {
        if (this.serviceType == null || this.serviceType == ServiceType.OTHER) {
            // Default to 3 months for "Other" service types
            this.nextServiceDate = new Date(this.serviceDate.getTime() + (90L * 24 * 60 * 60 * 1000));
            return;
        }

        // Calculate months until next service based on mileage interval and average miles driven
        int mileageInterval = this.serviceType.getRecommendedMileageInterval();

        // Prevent division by zero
        if (averageMilesPerMonth <= 0) {
            averageMilesPerMonth = 1000; // Default value
        }

        int monthsUntilNextService = (int) Math.ceil(mileageInterval / averageMilesPerMonth);

        // Ensure we have at least 1 month interval
        if (monthsUntilNextService < 1) {
            monthsUntilNextService = 1;
        }

        // Convert months to milliseconds (rough approximation)
        long nextServiceTime = this.serviceDate.getTime() + (monthsUntilNextService * 30L * 24 * 60 * 60 * 1000);
        this.nextServiceDate = new Date(nextServiceTime);
    }

    // Check if service is due or overdue
    public boolean isServiceDue() {
        if (this.completed) {
            return false; // Completed services are not due
        }

        Date currentDate = new Date();
        return this.nextServiceDate.before(currentDate);
    }

    // Check if service will be due within a certain number of days
    public boolean isServiceDueSoon(int daysThreshold) {
        if (this.completed) {
            return false; // Completed services are not due soon
        }

        long currentTime = System.currentTimeMillis();
        long dueSoonTime = currentTime + (daysThreshold * 24L * 60 * 60 * 1000);
        Date thresholdDate = new Date(dueSoonTime);

        // Service is due soon if it's after now but before the threshold
        return !this.nextServiceDate.before(new Date()) &&
                this.nextServiceDate.before(thresholdDate);
    }

    // Convert record to string representation for file storage
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(recordId).append(",");
        sb.append(carId).append(",");
        sb.append(userId).append(",");
        sb.append(serviceType).append(",");
        sb.append(DATE_FORMAT.format(serviceDate)).append(",");
        sb.append(DATE_FORMAT.format(nextServiceDate)).append(",");
        sb.append(mileage).append(",");
        // Replace commas in description with a placeholder to avoid CSV parsing issues
        sb.append(description.replace(",", "&#44;")).append(",");
        sb.append(cost).append(",");
        sb.append(completed);
        return sb.toString();
    }

    // Create ServiceRecord from string representation (from file)
    public static ServiceRecord fromFileString(String fileString) {
        try {
            String[] parts = fileString.split(",");
            if (parts.length < 10) {
                return null;
            }

            ServiceRecord record = new ServiceRecord();
            record.setRecordId(parts[0]);
            record.setCarId(parts[1]);
            record.setUserId(parts[2]);
            record.setServiceType(ServiceType.fromDescription(parts[3]));

            try {
                record.setServiceDate(DATE_FORMAT.parse(parts[4]));
                record.setNextServiceDate(DATE_FORMAT.parse(parts[5]));
            } catch (ParseException e) {
                // Use current date if parsing fails
                record.setServiceDate(new Date());
                record.setNextServiceDate(new Date(System.currentTimeMillis() + (90L * 24 * 60 * 60 * 1000)));
            }

            record.setMileage(Double.parseDouble(parts[6]));
            // Restore commas in description
            record.setDescription(parts[7].replace("&#44;", ","));
            record.setCost(Double.parseDouble(parts[8]));
            record.setCompleted(Boolean.parseBoolean(parts[9]));

            return record;
        } catch (Exception e) {
            System.err.println("Error parsing service record: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "ServiceRecord{" +
                "recordId='" + recordId + '\'' +
                ", carId='" + carId + '\'' +
                ", serviceType=" + serviceType +
                ", serviceDate=" + DATE_FORMAT.format(serviceDate) +
                ", nextServiceDate=" + DATE_FORMAT.format(nextServiceDate) +
                ", mileage=" + mileage +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", completed=" + completed +
                '}';
    }
}