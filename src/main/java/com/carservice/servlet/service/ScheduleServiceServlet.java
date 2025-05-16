package com.carservice.servlet.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for scheduling services - redirects to add-service with any provided parameters
 */
@WebServlet("/schedule-service")
public class ScheduleServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - redirect to add-service
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get any record ID or parameters
        String recordId = request.getParameter("recordId");

        // If a record ID is provided, we'll want to update that record
        if (recordId != null && !recordId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/update-service?recordId=" + recordId);
        } else {
            // Otherwise, just go to add service page
            response.sendRedirect(request.getContextPath() + "/add-service");
        }
    }
}