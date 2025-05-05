package com.carservice.model.service;

import com.carservice.util.FileHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletContext;

/**
 * ServiceManager class for managing service records
 */
public class ServiceManager {
    private static final String SERVICE_RECORDS_FILE = "service_records.txt";
    private ServiceLinkedList serviceRecords;
    private ServletContext servletContext;
    private String dataFilePath;

    // Constructor
    public ServiceManager() {
        this(null);
    }

    // Constructor with ServletContext
    public ServiceManager(ServletContext servletContext) {
        this.servletContext = servletContext;
        serviceRecords = new ServiceLinkedList();
        initializeFilePath();
        loadServiceRecords();
    }

    // Initialize file path
    private void initializeFilePath() {
        if (servletContext != null) {
            // Use WEB-INF/data within the application context
            String webInfDataPath = "/WEB-INF/data";
            dataFilePath = servletContext.getRealPath(webInfDataPath) + File.separator + SERVICE_RECORDS_FILE;

            // Make sure directory exists
            File dataDir = new File(servletContext.getRealPath(webInfDataPath));
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                System.out.println("Created WEB-INF/data directory: " + dataDir.getAbsolutePath() + " - Success: " + created);
            }
        } else {
            // Fallback to simple data directory if not in web context
            String dataPath = "data";
            dataFilePath = dataPath + File.separator + SERVICE_RECORDS_FILE;

            // Make sure directory exists
            File dataDir = new File(dataPath);
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                System.out.println("Created fallback data directory: " + dataPath + " - Success: " + created);
            }
        }

        System.out.println("ServiceManager: Using data file path: " + dataFilePath);
    }

    // Load service records from file
    private void loadServiceRecords() {
        File file = new File(dataFilePath);

        // If file doesn't exist, create it
        if (!file.exists()) {
            try {
                // Ensure parent directory exists
                file.getParentFile().mkdirs();
                boolean created = file.createNewFile();
                System.out.println("Created service records file: " + dataFilePath + " - Success: " + created);
            } catch (IOException e) {
                System.err.println("Error creating service records file: " + e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                ServiceRecord record = ServiceRecord.fromFileString(line);
                if (record != null) {
                    serviceRecords.add(record);
                    System.out.println("Loaded service record: " + record.getRecordId());
                }
            }
            System.out.println("Total service records loaded: " + serviceRecords.size());
        } catch (IOException e) {
            System.err.println("Error loading service records: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save service records to file
    private boolean saveServiceRecords() {
        try {
            // Ensure directory exists
            File file = new File(dataFilePath);
            if (!file.getParentFile().exists()) {
                boolean created = file.getParentFile().mkdirs();
                System.out.println("Created directory: " + file.getParentFile().getAbsolutePath() + " - Success: " + created);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
                for (ServiceRecord record : serviceRecords) {
                    writer.write(record.toFileString());
                    writer.newLine();
                }
            }
            System.out.println("Service records saved successfully to: " + dataFilePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving service records: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Add a new service record
    public boolean addServiceRecord(ServiceRecord record) {
        try {
            System.out.println("Adding new service record: " + record);

            // Generate a unique ID if not provided
            if (record.getRecordId() == null || record.getRecordId().isEmpty()) {
                record.setRecordId(UUID.randomUUID().toString());
            }

            serviceRecords.add(record);
            boolean saved = saveServiceRecords();
            System.out.println("Service record saved successfully: " + saved);
            return saved;
        } catch (Exception e) {
            System.err.println("Exception occurred when adding service record:");
            e.printStackTrace();
            return false;
        }
    }

    // Get service record by ID
    public ServiceRecord getServiceRecordById(String recordId) {
        return serviceRecords.findById(recordId);
    }

    // Get all service records
    public ServiceLinkedList getAllServiceRecords() {
        return serviceRecords;
    }

    // Get service records for a specific car
    public ServiceLinkedList getServiceRecordsByCarId(String carId) {
        return serviceRecords.getServicesByCarId(carId);
    }

    // Get service records for a specific user
    public ServiceLinkedList getServiceRecordsByUserId(String userId) {
        return serviceRecords.getServicesByUserId(userId);
    }

    // Update a service record
    public boolean updateServiceRecord(ServiceRecord updatedRecord) {
        boolean updated = serviceRecords.update(updatedRecord);
        if (updated) {
            return saveServiceRecords();
        }
        return false;
    }

    // Delete a service record
    public boolean deleteServiceRecord(String recordId) {
        ServiceRecord record = getServiceRecordById(recordId);
        if (record != null) {
            boolean removed = serviceRecords.remove(record);
            if (removed) {
                return saveServiceRecords();
            }
        }
        return false;
    }

    // Get upcoming/due service records
    public ServiceLinkedList getUpcomingServices(int daysThreshold) {
        ServiceLinkedList upcomingServices = serviceRecords.getUpcomingServices(daysThreshold);
        upcomingServices.sortByNextServiceDate(); // Sort by next service date
        return upcomingServices;
    }

    // Get overdue service records
    public ServiceLinkedList getOverdueServices() {
        ServiceLinkedList overdueServices = serviceRecords.getOverdueServices();
        overdueServices.sortByNextServiceDate(); // Sort by next service date
        return overdueServices;
    }

    // Get service history sorted by date
    public ServiceLinkedList getServiceHistory(String carId) {
        ServiceLinkedList history = serviceRecords.getServicesByCarId(carId);
        history.sortByServiceDate(); // Sort by service date
        return history;
    }

    // Mark a service as completed
    public boolean markServiceAsCompleted(String recordId) {
        ServiceRecord record = getServiceRecordById(recordId);
        if (record != null) {
            record.setCompleted(true);
            return updateServiceRecord(record);
        }
        return false;
    }

    // Reschedule a service
    public boolean rescheduleService(String recordId, Date newDate) {
        ServiceRecord record = getServiceRecordById(recordId);
        if (record != null) {
            record.setServiceDate(newDate);
            // We might need to recalculate the next service date here
            return updateServiceRecord(record);
        }
        return false;
    }

    // Set ServletContext (can be used to update the context after initialization)
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        initializeFilePath();
        // Reload service records with the new file path
        serviceRecords.clear();
        loadServiceRecords();
    }

    // Send notification for upcoming maintenance (polymorphic method)
    public String sendMaintenanceNotification(ServiceRecord record, boolean isPremiumUser) {
        if (isPremiumUser) {
            return sendPremiumNotification(record);
        } else {
            return sendRegularNotification(record);
        }
    }

    // Send notification for premium users (with more details)
    private String sendPremiumNotification(ServiceRecord record) {
        StringBuilder notification = new StringBuilder();
        notification.append("PRIORITY MAINTENANCE ALERT: ");
        notification.append(record.getServiceType().getDescription());
        notification.append(" is due on ");
        notification.append(new java.text.SimpleDateFormat("MMMM d, yyyy").format(record.getNextServiceDate()));
        notification.append(". This service is recommended at ");
        notification.append(record.getServiceType().getRecommendedMileageInterval());
        notification.append(" miles. Your current mileage: ");
        notification.append(record.getMileage());
        notification.append(" miles. ");
        notification.append("Special note: ");
        notification.append(record.getDescription());
        notification.append(" Please book your appointment soon to maintain your premium service benefits.");

        return notification.toString();
    }

    // Send notification for regular users (basic information)
    private String sendRegularNotification(ServiceRecord record) {
        StringBuilder notification = new StringBuilder();
        notification.append("Maintenance Reminder: ");
        notification.append(record.getServiceType().getDescription());
        notification.append(" is due on ");
        notification.append(new java.text.SimpleDateFormat("MM/dd/yyyy").format(record.getNextServiceDate()));
        notification.append(". Please schedule your service appointment.");

        return notification.toString();
    }
}