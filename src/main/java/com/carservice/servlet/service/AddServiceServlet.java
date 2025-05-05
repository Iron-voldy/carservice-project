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
 * Servlet for handling the addition of service records
 */
@WebServlet("/add-service")
public class AddServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Handles GET requests - display the add service form
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

        // Add service types to request for dropdown
        request.setAttribute("serviceTypes", ServiceType.values());

        // Forward to the add service page
        request.getRequestDispatcher("/service/add-service.jsp").forward(request, response);
    }

    /**
     * Handles POST requests - process the add service form
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

        try {
            // Get user ID from session
            String userId = (String) session.getAttribute("userId");

            // Get form parameters
            String carId = request.getParameter("carId");
            String serviceTypeStr = request.getParameter("serviceType");
            String serviceDateStr = request.getParameter("serviceDate");
            String mileageStr = request.getParameter("mileage");
            String description = request.getParameter("description");
            String costStr = request.getParameter("cost");
            String averageMilesPerMonthStr = request.getParameter("averageMilesPerMonth");

            // Validate input
            if (carId == null || serviceTypeStr == null || serviceDateStr == null ||
                    mileageStr == null || description == null || costStr == null ||
                    carId.trim().isEmpty() || serviceTypeStr.trim().isEmpty() ||
                    serviceDateStr.trim().isEmpty() || mileageStr.trim().isEmpty() ||
                    costStr.trim().isEmpty()) {

                request.setAttribute("errorMessage", "All fields are required");
                request.setAttribute("serviceTypes", ServiceType.values());
                request.getRequestDispatcher("/service/add-service.jsp").forward(request, response);
                return;
            }

            // Parse values
            ServiceType serviceType = ServiceType.fromDescription(serviceTypeStr);
            Date serviceDate = DATE_FORMAT.parse(serviceDateStr);
            double mileage = Double.parseDouble(mileageStr);
            double cost = Double.parseDouble(costStr);
            double averageMilesPerMonth = 1000; // Default value

            if (averageMilesPerMonthStr != null && !averageMilesPerMonthStr.trim().isEmpty()) {
                averageMilesPerMonth = Double.parseDouble(averageMilesPerMonthStr);
            }

            // Create service record
            ServiceRecord record = new ServiceRecord();
            record.setCarId(carId);
            record.setUserId(userId);
            record.setServiceType(serviceType);
            record.setServiceDate(serviceDate);
            record.setMileage(mileage);
            record.setDescription(description);
            record.setCost(cost);
            record.setCompleted(false);

            // Calculate next service date based on mileage and service type
            record.calculateNextServiceDate(averageMilesPerMonth);

            // Add service record
            ServiceManager serviceManager = new ServiceManager(getServletContext());
            boolean success = serviceManager.addServiceRecord(record);

            if (success) {
                // Redirect to service history page with success message
                session.setAttribute("successMessage", "Service record added successfully!");
                response.sendRedirect(request.getContextPath() + "/service-history");
            } else {
                // Set error message and go back to add service page
                request.setAttribute("errorMessage", "Failed to add service record. Please try again.");
                request.setAttribute("serviceTypes", ServiceType.values());
                request.getRequestDispatcher("/service/add-service.jsp").forward(request, response);
            }
        } catch (ParseException e) {
            // Handle date parsing error
            request.setAttribute("errorMessage", "Invalid date format. Please use YYYY-MM-DD format.");
            request.setAttribute("serviceTypes", ServiceType.values());
            request.getRequestDispatcher("/service/add-service.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Handle number parsing error
            request.setAttribute("errorMessage", "Invalid number format for mileage or cost.");
            request.setAttribute("serviceTypes", ServiceType.values());
            request.getRequestDispatcher("/service/add-service.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle other errors
            System.err.println("Error in AddServiceServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.setAttribute("serviceTypes", ServiceType.values());
            request.getRequestDispatcher("/service/add-service.jsp").forward(request, response);
        }
    }
}