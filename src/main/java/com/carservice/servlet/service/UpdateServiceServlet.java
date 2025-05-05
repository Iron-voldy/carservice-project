package com.carservice.servlet.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.carservice.model.service.ServiceManager;
import com.carservice.model.service.ServiceRecord;
import com.carservice.model.service.ServiceType;

/**
 * Servlet for updating service records
 */
@WebServlet("/update-service")
public class UpdateServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Handles GET requests - display the update service form
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

        // Get record ID from request parameter
        String recordId = request.getParameter("recordId");
        if (recordId == null || recordId.trim().isEmpty()) {
            // No record ID provided
            response.sendRedirect(request.getContextPath() + "/service-history");
            return;
        }

        try {
            // Create ServiceManager and get service record
            ServiceManager serviceManager = new ServiceManager(getServletContext());
            ServiceRecord record = serviceManager.getServiceRecordById(recordId);

            if (record == null) {
                // Record not found
                session.setAttribute("errorMessage", "Service record not found.");
                response.sendRedirect(request.getContextPath() + "/service-history");
                return;
            }

            // Add service record and service types to request
            request.setAttribute("serviceRecord", record);
            request.setAttribute("serviceTypes", ServiceType.values());

            // Forward to the update service page
            request.getRequestDispatcher("/service/update-service.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle errors
            System.err.println("Error in UpdateServiceServlet (GET): " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/service-history");
        }
    }

    /**
     * Handles POST requests - process the update service form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            // Not logged in, redirect to login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get record ID from request parameter
        String recordId = request.getParameter("recordId");
        if (recordId == null || recordId.trim().isEmpty()) {
            // No record ID provided
            response.sendRedirect(request.getContextPath() + "/service-history");
            return;
        }

        try {
            // Create ServiceManager and get service record
            ServiceManager serviceManager = new ServiceManager(getServletContext());
            ServiceRecord record = serviceManager.getServiceRecordById(recordId);

            if (record == null) {
                // Record not found
                session.setAttribute("errorMessage", "Service record not found.");
                response.sendRedirect(request.getContextPath() + "/service-history");
                return;
            }

            // Get form parameters
            String serviceTypeStr = request.getParameter("serviceType");
            String serviceDateStr = request.getParameter("serviceDate");
            String nextServiceDateStr = request.getParameter("nextServiceDate");
            String mileageStr = request.getParameter("mileage");
            String description = request.getParameter("description");
            String costStr = request.getParameter("cost");
            String completedStr = request.getParameter("completed");
            String averageMilesPerMonthStr = request.getParameter("averageMilesPerMonth");

            // Validate input
            if (serviceTypeStr == null || serviceDateStr == null || mileageStr == null ||
                    description == null || costStr == null || serviceTypeStr.trim().isEmpty() ||
                    serviceDateStr.trim().isEmpty() || mileageStr.trim().isEmpty() ||
                    costStr.trim().isEmpty()) {

                request.setAttribute("errorMessage", "All fields are required");
                request.setAttribute("serviceRecord", record);
                request.setAttribute("serviceTypes", ServiceType.values());
                request.getRequestDispatcher("/service/update-service.jsp").forward(request, response);
                return;
            }

            // Parse values
            ServiceType serviceType = ServiceType.fromDescription(serviceTypeStr);
            Date serviceDate = DATE_FORMAT.parse(serviceDateStr);
            Date nextServiceDate = null;
            if (nextServiceDateStr != null && !nextServiceDateStr.trim().isEmpty()) {
                nextServiceDate = DATE_FORMAT.parse(nextServiceDateStr);
            }
            double mileage = Double.parseDouble(mileageStr);
            double cost = Double.parseDouble(costStr);
            boolean completed = "on".equals(completedStr) || "true".equals(completedStr);
            double averageMilesPerMonth = 1000; // Default value

            if (averageMilesPerMonthStr != null && !averageMilesPerMonthStr.trim().isEmpty()) {
                averageMilesPerMonth = Double.parseDouble(averageMilesPerMonthStr);
            }

            // Update service record
            record.setServiceType(serviceType);
            record.setServiceDate(serviceDate);
            record.setMileage(mileage);
            record.setDescription(description);
            record.setCost(cost);
            record.setCompleted(completed);

            // Calculate next service date if not provided
            if (nextServiceDate != null) {
                record.setNextServiceDate(nextServiceDate);
            } else {
                record.calculateNextServiceDate(averageMilesPerMonth);
            }

            // Update record in database
            boolean success = serviceManager.updateServiceRecord(record);

            if (success) {
                // Redirect to service history page with success message
                session.setAttribute("successMessage", "Service record updated successfully!");
                response.sendRedirect(request.getContextPath() + "/service-history");
            } else {
                // Set error message and go back to update service page
                request.setAttribute("errorMessage", "Failed to update service record. Please try again.");
                request.setAttribute("serviceRecord", record);
                request.setAttribute("serviceTypes", ServiceType.values());
                request.getRequestDispatcher("/service/update-service.jsp").forward(request, response);
            }
        } catch (ParseException e) {
            // Handle date parsing error
            request.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD format.");
            request.setAttribute("serviceTypes", ServiceType.values());
            request.getRequestDispatcher("/service/update-service.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Handle number parsing error
            request.setAttribute("errorMessage", "Invalid number format for mileage or cost.");
            request.setAttribute("serviceTypes", ServiceType.values());
            request.getRequestDispatcher("/service/update-service.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle other errors
            System.err.println("Error in UpdateServiceServlet (POST): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/service/update-service.jsp").forward(request, response);
        }
    }
}