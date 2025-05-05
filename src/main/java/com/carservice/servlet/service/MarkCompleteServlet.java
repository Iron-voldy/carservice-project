package com.carservice.servlet.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.carservice.model.service.ServiceManager;
import com.carservice.model.service.ServiceRecord;

/**
 * Servlet for marking a service record as completed
 */
@WebServlet("/mark-complete")
public class MarkCompleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - mark a service as completed and redirect
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

        // Get return URL (where to redirect after completion)
        String returnUrl = request.getParameter("returnUrl");
        if (returnUrl == null || returnUrl.trim().isEmpty()) {
            // Default return URL
            returnUrl = request.getContextPath() + "/service-history";
        } else {
            // Make sure return URL is relative for security
            if (returnUrl.startsWith("http") || returnUrl.startsWith("//")) {
                returnUrl = request.getContextPath() + "/service-history";
            }
        }

        try {
            // Create ServiceManager and get service record
            ServiceManager serviceManager = new ServiceManager(getServletContext());

            // Mark the service as completed
            boolean success = serviceManager.markServiceAsCompleted(recordId);

            if (success) {
                // Set success message
                session.setAttribute("successMessage", "Service record marked as completed successfully!");
            } else {
                // Set error message
                session.setAttribute("errorMessage", "Failed to mark service record as completed. Please try again.");
            }

            // Redirect back to the appropriate page
            response.sendRedirect(returnUrl);
        } catch (Exception e) {
            // Handle errors
            System.err.println("Error in MarkCompleteServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect(returnUrl);
        }
    }
}