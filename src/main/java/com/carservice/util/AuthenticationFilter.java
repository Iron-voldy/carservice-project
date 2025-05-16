package com.carservice.util;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get session without creating a new one if it doesn't exist
        HttpSession session = httpRequest.getSession(false);

        if (session == null) {
            // Create a new session if one doesn't exist
            session = httpRequest.getSession(true);
            session.setAttribute("userId", "default-user-001");
            session.setAttribute("username", "Kodagoda Puthraya");
            session.setAttribute("isPremiumUser", false);
        } else if (session.getAttribute("userId") == null) {
            // Set default user attributes if userId is not set
            session.setAttribute("userId", "default-user-001");
            session.setAttribute("username", "Kodagoda Puthraya");
            session.setAttribute("isPremiumUser", false);
        }

        // Continue the filter chain BEFORE writing to the response
        chain.doFilter(request, response);

        // Don't perform any operations on the response after the chain is processed
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}