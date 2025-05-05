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
 * Servlet for deleting service records
 */
@WebServlet("/delete-service")
public class DeleteServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - display delete confirmation
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

            // Add service record to request
            request.setAttribute("serviceRecord", record);

            // Forward to the delete confirmation page
            request.getRequestDispatcher("/service/delete-service.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle errors
            System.err.println("Error in DeleteServiceServlet (GET): " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/service-history");
        }
    }

    /**
     * Handles POST requests - process the delete operation
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

        // Get confirmation parameter
        String confirmDelete = request.getParameter("confirmDelete");
        if (!"yes".equals(confirmDelete)) {
            // User did not confirm deletion
            response.sendRedirect(request.getContextPath() + "/service-history");
            return;
        }

        try {
            // Create ServiceManager and delete service record
            ServiceManager serviceManager = new ServiceManager(getServletContext());
            boolean deleted = serviceManager.deleteServiceRecord(recordId);

            if (deleted) {
                // Redirect to service history page with success message
                session.setAttribute("successMessage", "Service record deleted successfully!");
            } else {
                // Redirect to service history page with error message
                session.setAttribute("errorMessage", "Failed to delete service record. Please try again.");
            }

            response.sendRedirect(request.getContextPath() + "/service-history");
        } catch (Exception e) {
            // Handle errors
            System.err.println("Error in DeleteServiceServlet (POST): " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/service-history");
        }
    }
}