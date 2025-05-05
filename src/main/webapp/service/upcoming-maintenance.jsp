<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upcoming Maintenance</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Car Service Tracker</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/service-history">Service History</a></li>
                    <li><a href="${pageContext.request.contextPath}/upcoming-maintenance" class="active">Upcoming Maintenance</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile">Your Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <main>
            <h2>Upcoming Maintenance</h2>

            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <p>${errorMessage}</p>
                </div>
            </c:if>

            <div class="filter-options">
                <form action="${pageContext.request.contextPath}/upcoming-maintenance" method="get" class="filter-form">
                    <div class="form-group inline">
                        <label for="carId">Filter by Car:</label>
                        <input type="text" id="carId" name="carId" value="${currentCarId}">
                    </div>

                    <div class="form-group inline">
                        <label for="days">Show services due in next:</label>
                        <select id="days" name="days">
                            <option value="7" ${daysThreshold == 7 ? 'selected' : ''}>7 days</option>
                            <option value="14" ${daysThreshold == 14 ? 'selected' : ''}>14 days</option>
                            <option value="30" ${daysThreshold == 30 ? 'selected' : ''}>30 days</option>
                            <option value="60" ${daysThreshold == 60 ? 'selected' : ''}>60 days</option>
                            <option value="90" ${daysThreshold == 90 ? 'selected' : ''}>90 days</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-secondary">Apply Filters</button>

                    <c:if test="${not empty currentCarId || daysThreshold != 30}">
                        <a href="${pageContext.request.contextPath}/upcoming-maintenance" class="btn btn-link">Clear Filters</a>
                    </c:if>
                </form>
            </div>

            <!-- Notifications Section -->
            <c:if test="${not empty notifications}">
                <div class="notifications-section">
                    <h3>Maintenance Alerts</h3>
                    <div class="notifications-list">
                        <c:forEach var="notification" items="${notifications.split('\\\\n')}">
                            <c:if test="${not empty notification}">
                                <div class="notification-item">
                                    <div class="notification-icon">
                                        <i class="fa fa-bell"></i>
                                    </div>
                                    <div class="notification-content">
                                        ${notification}
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <!-- Overdue Services Section -->
            <c:if test="${not empty overdueServices && overdueServices.size() > 0}">
                <div class="overdue-section">
                    <h3>Overdue Services</h3>
                    <table class="service-table overdue-table">
                        <thead>
                            <tr>
                                <th>Service Type</th>
                                <th>Car ID</th>
                                <th>Due Date</th>
                                <th>Days Overdue</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="record" items="${overdueServices}">
                                <tr>
                                    <td>${record.serviceType.description}</td>
                                    <td>${record.carId}</td>
                                    <td><fmt:formatDate value="${record.nextServiceDate}" pattern="yyyy-MM-dd" /></td>
                                    <td>
                                        <fmt:formatDate var="dueDate" value="${record.nextServiceDate}" pattern="yyyy-MM-dd" />
                                        <fmt:parseDate var="dueDateParsed" value="${dueDate}" pattern="yyyy-MM-dd" />
                                        <jsp:useBean id="now" class="java.util.Date" />
                                        <fmt:formatNumber value="${(now.time - dueDateParsed.time) / (1000 * 60 * 60 * 24)}" pattern="#0" />
                                    </td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/update-service?recordId=${record.recordId}" class="btn btn-sm btn-primary">Schedule</a>
                                        <a href="${pageContext.request.contextPath}/mark-complete?recordId=${record.recordId}" class="btn btn-sm btn-success">Mark Complete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <!-- Upcoming Services Section -->
            <div class="upcoming-section">
                <h3>Upcoming Services (Next ${daysThreshold} Days)</h3>

                <c:choose>
                    <c:when test="${not empty upcomingServices && upcomingServices.size() > 0}">
                        <table class="service-table upcoming-table">
                            <thead>
                                <tr>
                                    <th>Service Type</th>
                                    <th>Car ID</th>
                                    <th>Due Date</th>
                                    <th>Days Until Due</th>
                                    <th>Recommended Interval</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="record" items="${upcomingServices}">
                                    <tr>
                                        <td>${record.serviceType.description}</td>
                                        <td>${record.carId}</td>
                                        <td><fmt:formatDate value="${record.nextServiceDate}" pattern="yyyy-MM-dd" /></td>
                                        <td>
                                            <fmt:formatDate var="dueDate" value="${record.nextServiceDate}" pattern="yyyy-MM-dd" />
                                            <fmt:parseDate var="dueDateParsed" value="${dueDate}" pattern="yyyy-MM-dd" />
                                            <jsp:useBean id="today" class="java.util.Date" />
                                            <fmt:formatNumber value="${(dueDateParsed.time - today.time) / (1000 * 60 * 60 * 24)}" pattern="#0" />
                                        </td>
                                        <td>${record.serviceType.recommendedMileageInterval} miles</td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/update-service?recordId=${record.recordId}" class="btn btn-sm btn-primary">Reschedule</a>
                                            <a href="${pageContext.request.contextPath}/mark-complete?recordId=${record.recordId}" class="btn btn-sm btn-success">Mark Complete</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <div class="no-records">
                            <p>No upcoming maintenance tasks due in the next ${daysThreshold} days.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="service-actions">
                <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">Add New Service Record</a>
                <a href="${pageContext.request.contextPath}/service-history" class="btn btn-secondary">View Complete Service History</a>
            </div>
        </main>

        <footer>
            <p>&copy; 2025 Car Service Tracker - All Rights Reserved</p>
        </footer>
    </div>
</body>
</html>