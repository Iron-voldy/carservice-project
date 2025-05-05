package com.carservice.servlet.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.carservice.model.service.ServiceLinkedList;
import com.carservice.model.service.ServiceManager;
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

            // Add service records to request
            request.setAttribute("serviceRecords", serviceRecords);
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
}