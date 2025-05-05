<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CarCare - Upcoming Maintenance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Main Container -->
    <div class="main-container">
        <!-- Top Navigation Bar -->
        <header class="top-nav">
            <div class="logo-section">
                <a href="${pageContext.request.contextPath}/index.jsp" class="logo">
                    <i class="fas fa-car"></i> CarCare
                </a>
            </div>
            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">
                    <i class="fas fa-home"></i> Services
                </a>
                <a href="${pageContext.request.contextPath}/service-history" class="nav-link">
                    <i class="fas fa-car"></i> My Vehicles
                </a>
                <a href="${pageContext.request.contextPath}/upcoming-maintenance" class="nav-link active">
                    <i class="fas fa-history"></i> History
                </a>
            </nav>
            <div class="user-section">
                <div class="dropdown">
                    <button class="btn btn-outline dropdown-toggle">
                        <i class="fas fa-user-circle"></i> ${sessionScope.username}
                        <i class="fas fa-chevron-down"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="${pageContext.request.contextPath}/profile">Profile</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="main-content">
            <!-- Page Header -->
            <div class="page-header">
                <h1><i class="fas fa-calendar-alt"></i> Upcoming Maintenance</h1>
                <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Schedule Service
                </a>
            </div>

            <!-- Alert Messages -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                    <button class="close-btn"><i class="fas fa-times"></i></button>
                </div>
            </c:if>

            <!-- Filter Section -->
            <div class="filter-section">
                <form action="${pageContext.request.contextPath}/upcoming-maintenance" method="get" class="filter-form">
                    <div class="form-group">
                        <label for="carId">Vehicle:</label>
                        <select id="carId" name="carId">
                            <option value="">All Vehicles</option>
                            <c:forEach var="car" items="${cars}">
                                <option value="${car.id}" ${currentCarId == car.id ? 'selected' : ''}>${car.make} ${car.model} (${car.year})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="days">Time Period:</label>
                        <select id="days" name="days">
                            <option value="7" ${daysThreshold == 7 ? 'selected' : ''}>Next 7 days</option>
                            <option value="14" ${daysThreshold == 14 ? 'selected' : ''}>Next 14 days</option>
                            <option value="30" ${daysThreshold == 30 ? 'selected' : ''}>Next 30 days</option>
                            <option value="60" ${daysThreshold == 60 ? 'selected' : ''}>Next 60 days</option>
                            <option value="90" ${daysThreshold == 90 ? 'selected' : ''}>Next 90 days</option>
                        </select>
                    </div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-filter"></i> Apply Filters
                        </button>
                        <c:if test="${not empty currentCarId || daysThreshold != 30}">
                            <a href="${pageContext.request.contextPath}/upcoming-maintenance" class="btn btn-outline">
                                <i class="fas fa-times"></i> Clear Filters
                            </a>
                        </c:if>
                    </div>
                </form>
            </div>

            <!-- Overdue Services Section -->
            <c:if test="${not empty overdueServices && overdueServices.size() > 0}">
                <section class="overdue-services-section">
                    <div class="section-header alert-header">
                        <h2><i class="fas fa-exclamation-triangle"></i> Overdue Services</h2>
                    </div>
                    <div class="table-responsive">
                        <table class="data-table alert-table">
                            <thead>
                                <tr>
                                    <th>Service Type</th>
                                    <th>Vehicle</th>
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
                                        <td class="highlight-cell">
                                            <fmt:formatDate value="${record.nextServiceDate}" pattern="MMM dd, yyyy" />
                                        </td>
                                        <td>
                                            <span class="badge badge-danger">
                                                <fmt:formatDate var="dueDate" value="${record.nextServiceDate}" pattern="yyyy-MM-dd" />
                                                <fmt:parseDate var="dueDateParsed" value="${dueDate}" pattern="yyyy-MM-dd" />
                                                <jsp:useBean id="now" class="java.util.Date" />
                                                <fmt:formatNumber value="${(now.time - dueDateParsed.time) / (1000 * 60 * 60 * 24)}" pattern="#0" /> days
                                            </span>
                                        </td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/update-service?recordId=${record.recordId}"
                                               class="btn btn-sm btn-primary" title="Schedule">
                                                <i class="fas fa-calendar-plus"></i> Schedule
                                            </a>
                                            <a href="${pageContext.request.contextPath}/mark-complete?recordId=${record.recordId}"
                                               class="btn btn-sm btn-success" title="Mark Complete">
                                                <i class="fas fa-check"></i> Complete
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </section>
            </c:if>

            <!-- Upcoming Services Section -->
            <section class="upcoming-services-section">
                <div class="section-header">
                    <h2><i class="fas fa-calendar-check"></i> Upcoming Services (Next ${daysThreshold} Days)</h2>
                </div>

                <c:choose>
                    <c:when test="${not empty upcomingServices && upcomingServices.size() > 0}">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Service Type</th>
                                        <th>Vehicle</th>
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
                                            <td>
                                                <fmt:formatDate value="${record.nextServiceDate}" pattern="MMM dd, yyyy" />
                                            </td>
                                            <td>
                                                <span class="badge badge-info">
                                                    <fmt:formatDate var="dueDate" value="${record.nextServiceDate}" pattern="yyyy-MM-dd" />
                                                    <fmt:parseDate var="dueDateParsed" value="${dueDate}" pattern="yyyy-MM-dd" />
                                                    <jsp:useBean id="today" class="java.util.Date" />
                                                    <fmt:formatNumber value="${(dueDateParsed.time - today.time) / (1000 * 60 * 60 * 24)}" pattern="#0" /> days
                                                </span>
                                            </td>
                                            <td>${record.serviceType.recommendedMileageInterval} miles</td>
                                            <td class="actions">
                                                <a href="${pageContext.request.contextPath}/update-service?recordId=${record.recordId}"
                                                   class="btn btn-sm btn-primary" title="Reschedule">
                                                    <i class="fas fa-calendar-alt"></i> Reschedule
                                                </a>
                                                <a href="${pageContext.request.contextPath}/mark-complete?recordId=${record.recordId}"
                                                   class="btn btn-sm btn-success" title="Mark Complete">
                                                    <i class="fas fa-check"></i> Complete
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="fas fa-calendar-check"></i>
                            </div>
                            <p>No upcoming maintenance tasks due in the next ${daysThreshold} days.</p>
                            <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Schedule Service
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>

            <!-- Notifications Section -->
            <c:if test="${not empty notifications}">
                <section class="notifications-section">
                    <div class="section-header">
                        <h2><i class="fas fa-bell"></i> Maintenance Alerts</h2>
                    </div>
                    <div class="notification-cards">
                        <c:forEach var="notification" items="${notifications.split('\\\\n')}">
                            <c:if test="${not empty notification}">
                                <div class="notification-card">
                                    <div class="notification-icon">
                                        <i class="fas fa-bell"></i>
                                    </div>
                                    <div class="notification-content">
                                        ${notification}
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </section>
            </c:if>
        </main>

        <!-- Footer -->
        <footer class="footer">
            <div class="footer-content">
                <p>&copy; 2025 CarCare - Car Service Maintenance Tracker</p>
                <div class="footer-links">
                    <a href="#">Privacy Policy</a>
                    <a href="#">Terms of Service</a>
                    <a href="#">Contact Us</a>
                </div>
            </div>
        </footer>
    </div>

    <script>
        // Close alert messages
        document.querySelectorAll('.close-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                btn.parentElement.style.display = 'none';
            });
        });
    </script>
</body>
</html>