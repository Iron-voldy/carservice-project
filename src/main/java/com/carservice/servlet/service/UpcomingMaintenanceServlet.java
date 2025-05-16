package com.carservice.servlet.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.carservice.model.service.ServiceType;
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
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            // Not logged in, redirect to login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get user ID from session
        String userId = (String) session.getAttribute("userId");

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

            // Get all service records for this user first
            ServiceLinkedList userServices = serviceManager.getServiceRecordsByUserId(userId);
            System.out.println("User services retrieved: " + userServices.size());

            // Filter by car if specified
            ServiceLinkedList filteredServices = userServices;
            if (carId != null && !carId.trim().isEmpty()) {
                filteredServices = userServices.getServicesByCarId(carId);
                System.out.println("Filtered services by car ID: " + filteredServices.size());
            }

            // Get upcoming services that are not completed
            ServiceLinkedList upcomingServices = new ServiceLinkedList();
            for (ServiceRecord record : filteredServices) {
                if (!record.isCompleted() && record.isServiceDueSoon(daysThreshold)) {
                    upcomingServices.add(record);
                }
            }
            System.out.println("Upcoming services: " + upcomingServices.size());

            // Get overdue services that are not completed
            ServiceLinkedList overdueServices = new ServiceLinkedList();
            for (ServiceRecord record : filteredServices) {
                if (!record.isCompleted() && record.isServiceDue()) {
                    overdueServices.add(record);
                }
            }
            System.out.println("Overdue services: " + overdueServices.size());

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

            // If there's no data, add sample data for demonstration
            if (upcomingServicesList.isEmpty() && overdueServicesList.isEmpty()) {
                upcomingServicesList = createSampleUpcomingServices(userId);
                overdueServicesList = createSampleOverdueServices(userId);

                System.out.println("Added sample data for demonstration");
                System.out.println("Sample upcoming services: " + upcomingServicesList.size());
                System.out.println("Sample overdue services: " + overdueServicesList.size());
            }

            // Check if user is premium (for different notification methods)
            Boolean isPremiumUser = (Boolean) session.getAttribute("isPremiumUser");
            if (isPremiumUser == null) {
                isPremiumUser = false; // Default to regular user
            }

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
            cars.add(new Car("CAR004", "Nissan", "Altima", "2019"));
            cars.add(new Car("CAR005", "Tesla", "Model 3", "2023"));
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

    /**
     * Creates sample upcoming service records for demonstration
     */
    private List<ServiceRecord> createSampleUpcomingServices(String userId) {
        List<ServiceRecord> services = new ArrayList<>();

        try {
            // Current date for calculations
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Sample upcoming service 1 - Oil Change in 10 days
            ServiceRecord record1 = new ServiceRecord();
            record1.setRecordId("SR001");
            record1.setCarId("CAR001");
            record1.setUserId(userId);
            record1.setServiceType(ServiceType.OIL_CHANGE);
            record1.setServiceDate(dateFormat.parse("2025-04-20"));
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 10);
            record1.setNextServiceDate(cal.getTime());
            record1.setMileage(5000);
            record1.setDescription("Regular oil change with synthetic oil and filter replacement");
            record1.setCost(59.99);
            record1.setCompleted(false);
            services.add(record1);

            // Sample upcoming service 2 - Tire Rotation in 15 days
            ServiceRecord record2 = new ServiceRecord();
            record2.setRecordId("SR002");
            record2.setCarId("CAR002");
            record2.setUserId(userId);
            record2.setServiceType(ServiceType.TIRE_ROTATION);
            record2.setServiceDate(dateFormat.parse("2025-04-15"));
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 15);
            record2.setNextServiceDate(cal.getTime());
            record2.setMileage(12400);
            record2.setDescription("Rotate all four tires and check tire pressure");
            record2.setCost(29.99);
            record2.setCompleted(false);
            services.add(record2);

            // Sample upcoming service 3 - Air Filter in 20 days
            ServiceRecord record3 = new ServiceRecord();
            record3.setRecordId("SR003");
            record3.setCarId("CAR003");
            record3.setUserId(userId);
            record3.setServiceType(ServiceType.AIR_FILTER);
            record3.setServiceDate(dateFormat.parse("2025-03-10"));
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 20);
            record3.setNextServiceDate(cal.getTime());
            record3.setMileage(22500);
            record3.setDescription("Replace engine air filter to improve fuel efficiency");
            record3.setCost(24.99);
            record3.setCompleted(false);
            services.add(record3);

        } catch (Exception e) {
            System.err.println("Error creating sample upcoming services: " + e.getMessage());
        }

        return services;
    }

    /**
     * Creates sample overdue service records for demonstration
     */
    private List<ServiceRecord> createSampleOverdueServices(String userId) {
        List<ServiceRecord> services = new ArrayList<>();

        try {
            // Current date for calculations
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Sample overdue service 1 - Brake Service overdue by 5 days
            ServiceRecord record1 = new ServiceRecord();
            record1.setRecordId("SR004");
            record1.setCarId("CAR001");
            record1.setUserId(userId);
            record1.setServiceType(ServiceType.BRAKE_SERVICE);
            record1.setServiceDate(dateFormat.parse("2025-01-15"));
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, -5);  // 5 days overdue
            record1.setNextServiceDate(cal.getTime());
            record1.setMileage(18000);
            record1.setDescription("Inspect brake pads, rotors, and brake fluid");
            record1.setCost(149.99);
            record1.setCompleted(false);
            services.add(record1);

            // Sample overdue service 2 - Fluid Check overdue by 10 days
            ServiceRecord record2 = new ServiceRecord();
            record2.setRecordId("SR005");
            record2.setCarId("CAR003");
            record2.setUserId(userId);
            record2.setServiceType(ServiceType.FLUID_CHECK);
            record2.setServiceDate(dateFormat.parse("2025-02-10"));
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, -10);  // 10 days overdue
            record2.setNextServiceDate(cal.getTime());
            record2.setMileage(32000);
            record2.setDescription("Check and top off all vehicle fluids");
            record2.setCost(19.99);
            record2.setCompleted(false);
            services.add(record2);

        } catch (Exception e) {
            System.err.println("Error creating sample overdue services: " + e.getMessage());
        }

        return services;
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