package com.carservice.servlet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.carservice.model.service.ServiceLinkedList;
import com.carservice.model.service.ServiceManager;
import com.carservice.model.service.ServiceRecord;
import com.carservice.util.ServiceSortAlgorithm;

/**
 * Servlet for viewing upcoming maintenance tasks
 */
@WebServlet("/upcoming-maintenance")
public class UpcomingMaintenanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - display upcoming maintenance
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get user ID from session (using default)
        String userId = "default-user-001";

        // Get car ID from request parameter (if available)
        String carId = request.getParameter("carId");

        // Get days threshold from request parameter (default to 30 days)
        int daysThreshold = 30;
        String daysParam = request.getParameter("days");
        if (daysParam != null && !daysParam.trim().isEmpty()) {
            try {
                daysThreshold = Integer.parseInt(daysParam);
            } catch (NumberFormatException e) {
                // Use default if parsing fails
                System.err.println("Invalid days parameter: " + daysParam + ". Using default value of 30 days.");
            }
        }

        try {
            // Create ServiceManager
            ServiceManager serviceManager = new ServiceManager(getServletContext());
            System.out.println("ServiceManager created successfully");

            // Get all service records
            ServiceLinkedList allServices = serviceManager.getAllServiceRecords();
            System.out.println("All services retrieved: " + allServices.size());

            // Filter by car if specified
            ServiceLinkedList filteredServices = allServices;
            if (carId != null && !carId.trim().isEmpty()) {
                filteredServices = allServices.getServicesByCarId(carId);
                System.out.println("Filtered services by car ID: " + filteredServices.size());
            }

            // Current date
            Date currentDate = new Date();

            // Calculate future threshold date
            long futureDateMillis = currentDate.getTime() + (daysThreshold * 24L * 60 * 60 * 1000);
            Date futureDate = new Date(futureDateMillis);

            // Get upcoming services that are not completed
            ServiceLinkedList upcomingServices = new ServiceLinkedList();
            for (ServiceRecord record : filteredServices) {
                if (!record.isCompleted() &&
                        record.getNextServiceDate() != null &&
                        record.getNextServiceDate().after(currentDate) &&
                        record.getNextServiceDate().before(futureDate)) {
                    upcomingServices.add(record);
                    System.out.println("Adding upcoming service: " + record.getRecordId() +
                            ", type: " + record.getServiceType().getDescription() +
                            ", next service date: " + record.getNextServiceDate());
                }
            }
            System.out.println("Upcoming services: " + upcomingServices.size());

            // Get overdue services that are not completed
            ServiceLinkedList overdueServices = new ServiceLinkedList();
            for (ServiceRecord record : filteredServices) {
                if (!record.isCompleted() &&
                        record.getNextServiceDate() != null &&
                        record.getNextServiceDate().before(currentDate)) {
                    overdueServices.add(record);
                    System.out.println("Adding overdue service: " + record.getRecordId() +
                            ", type: " + record.getServiceType().getDescription() +
                            ", next service date: " + record.getNextServiceDate());
                }
            }
            System.out.println("Overdue services: " + overdueServices.size());

            // If no records found, add a few samples for testing
            if (upcomingServices.size() == 0 && overdueServices.size() == 0) {
                System.out.println("No records found, forcing all non-completed records to show");

                // Add all non-completed records
                for (ServiceRecord record : filteredServices) {
                    if (!record.isCompleted()) {
                        // For demo, show some as upcoming and some as overdue
                        if (upcomingServices.size() < 2) {
                            upcomingServices.add(record);
                            System.out.println("Adding forced upcoming service: " + record.getRecordId());
                        } else {
                            overdueServices.add(record);
                            System.out.println("Adding forced overdue service: " + record.getRecordId());
                        }
                    }
                }
            }

            // Sort by next service date
            ServiceSortAlgorithm.selectionSortByNextServiceDate(upcomingServices, true);
            ServiceSortAlgorithm.selectionSortByNextServiceDate(overdueServices, true);

            // Convert linked lists to ArrayList for JSTL compatibility
            List<ServiceRecord> upcomingServicesList = new ArrayList<>();
            for (ServiceRecord record : upcomingServices) {
                upcomingServicesList.add(record);
            }

            List<ServiceRecord> overdueServicesList = new ArrayList<>();
            for (ServiceRecord record : overdueServices) {
                overdueServicesList.add(record);
            }

            // Check if user is premium (for different notification methods)
            Boolean isPremiumUser = false;

            // Generate notifications for upcoming services
            StringBuilder notifications = new StringBuilder();
            for (ServiceRecord record : upcomingServicesList) {
                String notification = serviceManager.sendMaintenanceNotification(record, isPremiumUser);
                if (notification != null) {
                    notifications.append(notification);
                    notifications.append("\\n"); // Newline character for JSP display
                }
            }

            // Add vehicles for dropdown
            List<Car> cars = new ArrayList<>();
            cars.add(new Car("CAR001", "Toyota", "Camry", "2022"));
            cars.add(new Car("CAR002", "Honda", "Civic", "2020"));
            cars.add(new Car("CAR003", "Ford", "F-150", "2021"));
            request.setAttribute("cars", cars);

            // Add attributes to request
            request.setAttribute("upcomingServices", upcomingServicesList);
            request.setAttribute("overdueServices", overdueServicesList);
            request.setAttribute("daysThreshold", daysThreshold);
            request.setAttribute("notifications", notifications.toString());
            request.setAttribute("currentCarId", carId);

            // Debug information
            System.out.println("Sending to JSP - upcoming services: " + upcomingServicesList.size());
            System.out.println("Sending to JSP - overdue services: " + overdueServicesList.size());

            // Forward to upcoming maintenance page
            request.getRequestDispatcher("/service/upcoming-maintenance.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle errors
            System.err.println("Error in UpcomingMaintenanceServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while retrieving upcoming maintenance: " + e.getMessage());
            request.getRequestDispatcher("/service/upcoming-maintenance.jsp").forward(request, response);
        }
    }

    // Simple Car class for dropdown
    public static class Car {
        private String id;
        private String make;
        private String model;
        private String year;

        public Car(String id, String make, String model, String year) {
            this.id = id;
            this.make = make;
            this.model = model;
            this.year = year;
        }

        public String getId() { return id; }
        public String getMake() { return make; }
        public String getModel() { return model; }
        public String getYear() { return year; }
    }
}