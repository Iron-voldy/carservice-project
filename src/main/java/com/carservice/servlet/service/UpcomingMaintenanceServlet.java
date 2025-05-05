package com.carservice.servlet.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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
            }
        }

        try {
            // Create ServiceManager
            ServiceManager serviceManager = new ServiceManager(getServletContext());

            // Get upcoming maintenance records
            ServiceLinkedList upcomingServices;

            if (carId != null && !carId.trim().isEmpty()) {
                // Get upcoming services for specific car
                ServiceLinkedList carServices = serviceManager.getServiceRecordsByCarId(carId);
                upcomingServices = carServices.getUpcomingServices(daysThreshold);
            } else {
                // Get all upcoming services for this user
                ServiceLinkedList userServices = serviceManager.getServiceRecordsByUserId(userId);
                upcomingServices = userServices.getUpcomingServices(daysThreshold);
            }

            // Sort by next service date
            ServiceSortAlgorithm.selectionSortByNextServiceDate(upcomingServices, true);

            // Create date range for filtering (now to daysThreshold days from now)
            Calendar cal = Calendar.getInstance();
            Date startDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, daysThreshold);
            Date endDate = cal.getTime();

            // Get services due in the specified date range
            ServiceLinkedList servicesDueInRange =
                    ServiceSortAlgorithm.getServicesDueInRange(upcomingServices, startDate, endDate);

            // Get overdue services
            ServiceLinkedList overdueServices = serviceManager.getOverdueServices();
            if (carId != null && !carId.trim().isEmpty()) {
                // Filter overdue services for specific car
                overdueServices = overdueServices.getServicesByCarId(carId);
            } else {
                // Filter overdue services for this user
                overdueServices = overdueServices.getServicesByUserId(userId);
            }

            // Check if user is premium (for different notification methods)
            // For this example, we'll assume premium status is in the session
            Boolean isPremiumUser = (Boolean) session.getAttribute("isPremiumUser");
            if (isPremiumUser == null) {
                isPremiumUser = false; // Default to regular user
            }

            // Generate notifications for upcoming services
            StringBuilder notifications = new StringBuilder();
            for (ServiceRecord record : servicesDueInRange) {
                notifications.append(serviceManager.sendMaintenanceNotification(record, isPremiumUser));
                notifications.append("\\n"); // Newline character for JSP display
            }

            // Add attributes to request
            request.setAttribute("upcomingServices", servicesDueInRange);
            request.setAttribute("overdueServices", overdueServices);
            request.setAttribute("daysThreshold", daysThreshold);
            request.setAttribute("notifications", notifications.toString());
            request.setAttribute("currentCarId", carId);

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
}