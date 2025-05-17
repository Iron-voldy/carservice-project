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

import com.carservice.model.service.ServiceLinkedList;
import com.carservice.model.service.ServiceManager;
import com.carservice.model.service.ServiceRecord;
import com.carservice.util.ServiceSortAlgorithm;

/**
 * Servlet for dashboard (index page)
 */
@WebServlet("/dashboard")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests - display dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get user ID from session
        String userId = "default-user-001";

        try {
            // Create ServiceManager
            ServiceManager serviceManager = new ServiceManager(getServletContext());

            // Get all service records
            ServiceLinkedList allServices = serviceManager.getAllServiceRecords();

            // Current date
            Date currentDate = new Date();

            // Calculate 30 days in the future
            long futureDate30MilliSec = currentDate.getTime() + (30L * 24 * 60 * 60 * 1000);
            Date futureDate30 = new Date(futureDate30MilliSec);

            // Get upcoming services (non-completed services due within 30 days)
            ServiceLinkedList upcomingServices = new ServiceLinkedList();
            for (ServiceRecord record : allServices) {
                if (!record.isCompleted() &&
                        record.getNextServiceDate() != null &&
                        record.getNextServiceDate().after(currentDate) &&
                        record.getNextServiceDate().before(futureDate30)) {
                    upcomingServices.add(record);
                }
            }

            // Get overdue services (non-completed services with due date before today)
            ServiceLinkedList overdueServices = new ServiceLinkedList();
            for (ServiceRecord record : allServices) {
                if (!record.isCompleted() &&
                        record.getNextServiceDate() != null &&
                        record.getNextServiceDate().before(currentDate)) {
                    overdueServices.add(record);
                }
            }

            // Get recent services (most recent first)
            ServiceLinkedList recentServices = allServices;
            ServiceSortAlgorithm.selectionSortByDate(recentServices, false);

            // If no overdue or upcoming services found, use non-completed records for demo
            if (upcomingServices.size() == 0 && overdueServices.size() == 0) {
                System.out.println("No upcoming or overdue services found. Using non-completed records for demo.");
                for (ServiceRecord record : allServices) {
                    if (!record.isCompleted()) {
                        if (upcomingServices.size() < 2) {
                            upcomingServices.add(record);
                        } else {
                            overdueServices.add(record);
                        }
                    }
                }
            }

            // Convert LinkedLists to ArrayLists for JSTL compatibility
            List<ServiceRecord> upcomingList = new ArrayList<>();
            for (ServiceRecord record : upcomingServices) {
                upcomingList.add(record);
            }

            List<ServiceRecord> overdueList = new ArrayList<>();
            for (ServiceRecord record : overdueServices) {
                overdueList.add(record);
            }

            List<ServiceRecord> recentList = new ArrayList<>();
            int count = 0;
            for (ServiceRecord record : recentServices) {
                recentList.add(record);
                count++;
                if (count >= 5) break; // Only show top 5 recent services
            }

            // Add attributes to request
            request.setAttribute("upcomingServices", upcomingList);
            request.setAttribute("overdueServices", overdueList);
            request.setAttribute("recentServices", recentList);

            // Forward to index page
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in IndexServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}