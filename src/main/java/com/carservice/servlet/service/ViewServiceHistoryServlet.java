package com.carservice.servlet.service;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet for viewing service history
 */
@WebServlet("/service-history")
public class ViewServiceHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - display the service history
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

        // Get sort parameter (if available)
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        boolean ascending = "asc".equals(sortOrder);

        try {
            // Create ServiceManager and get service records
            ServiceManager serviceManager = new ServiceManager(getServletContext());
            ServiceLinkedList serviceRecords;

            if (carId != null && !carId.trim().isEmpty()) {
                // Get service records for specific car
                serviceRecords = serviceManager.getServiceRecordsByCarId(carId);
            } else {
                // Get all service records for this user
                serviceRecords = serviceManager.getServiceRecordsByUserId(userId);
            }

            // Sort service records based on sort parameter
            if (sortBy != null) {
                switch (sortBy) {
                    case "date":
                        ServiceSortAlgorithm.selectionSortByDate(serviceRecords, ascending);
                        break;
                    case "nextDate":
                        ServiceSortAlgorithm.selectionSortByNextServiceDate(serviceRecords, ascending);
                        break;
                    case "mileage":
                        ServiceSortAlgorithm.selectionSortByMileage(serviceRecords, ascending);
                        break;
                    case "cost":
                        ServiceSortAlgorithm.selectionSortByCost(serviceRecords, ascending);
                        break;
                    default:
                        // Default sort by service date (most recent first)
                        ServiceSortAlgorithm.selectionSortByDate(serviceRecords, false);
                        break;
                }
            } else {
                // Default sort by service date (most recent first)
                ServiceSortAlgorithm.selectionSortByDate(serviceRecords, false);
            }

            // Convert ServiceLinkedList to a standard ArrayList for JSTL compatibility
            List<ServiceRecord> recordsList = new ArrayList<>();
            for (ServiceRecord record : serviceRecords) {
                recordsList.add(record);
            }

            // Calculate statistics
            double totalCost = 0.0;
            double totalMileage = 0.0;
            int completedCount = 0;

            for (ServiceRecord record : recordsList) {
                totalCost += record.getCost();
                totalMileage += record.getMileage();
                if (record.isCompleted()) {
                    completedCount++;
                }
            }

            double avgMileage = recordsList.isEmpty() ? 0 : totalMileage / recordsList.size();

            // Add statistics to request
            request.setAttribute("totalCost", totalCost);
            request.setAttribute("avgMileage", avgMileage);
            request.setAttribute("completedCount", completedCount);

            // Add cars for dropdown (simulate for now)
            List<Car> cars = new ArrayList<>();
            cars.add(new Car("CAR001", "Toyota", "Camry", "2022"));
            cars.add(new Car("CAR002", "Honda", "Civic", "2020"));
            cars.add(new Car("CAR003", "Ford", "F-150", "2021"));
            cars.add(new Car("CAR004", "Nissan", "Altima", "2019"));
            cars.add(new Car("CAR005", "Tesla", "Model 3", "2023"));
            request.setAttribute("cars", cars);

            // Add service records to request as ArrayList (JSTL compatible)
            request.setAttribute("serviceRecords", recordsList);
            request.setAttribute("currentCarId", carId);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder);

            // Forward to the service history page
            request.getRequestDispatcher("/service/service-history.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle errors
            System.err.println("Error in ViewServiceHistoryServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while retrieving service history: " + e.getMessage());
            request.getRequestDispatcher("/service/service-history.jsp").forward(request, response);
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