<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CarCare - Dashboard</title>
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
                <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link active">
                    <i class="fas fa-home"></i> Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/service-history" class="nav-link">
                    <i class="fas fa-history"></i> Service History
                </a>
                <a href="${pageContext.request.contextPath}/upcoming-maintenance" class="nav-link">
                    <i class="fas fa-calendar-alt"></i> Upcoming Maintenance
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
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="main-content">
            <!-- Dashboard Header -->
            <div class="dashboard-header">
                <h1><i class="fas fa-tachometer-alt"></i> Dashboard</h1>
                <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">
                    <i class="fas fa-plus"></i> New Service
                </a>
            </div>

            <!-- Alert Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                    <button class="close-btn"><i class="fas fa-times"></i></button>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
                    <button class="close-btn"><i class="fas fa-times"></i></button>
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>

            <!-- Welcome Card -->
            <div class="welcome-card">
                <div class="welcome-icon">
                    <i class="fas fa-user-circle"></i>
                </div>
                <div class="welcome-text">
                    <h2>Welcome back, ${sessionScope.username}!</h2>
                    <p>Monitor your vehicle maintenance and keep track of service records</p>
                </div>
            </div>

            <!-- Dashboard Cards -->
            <div class="dashboard-cards">
                <!-- Service History Card -->
                <div class="dashboard-card">
                    <div class="card-header">
                        <h3>Service History</h3>
                        <i class="fas fa-history"></i>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty recentServices || recentServices.size() == 0}">
                            <div class="empty-state">
                                <div class="empty-icon">
                                    <i class="fas fa-history"></i>
                                </div>
                                <p>No service records found</p>
                                <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">Add Service Record</a>
                            </div>
                        </c:if>
                        <c:if test="${not empty recentServices && recentServices.size() > 0}">
                            <ul class="service-list">
                                <c:forEach var="service" items="${recentServices}" end="4">
                                    <li>
                                        <div class="service-info">
                                            <span class="service-type">${service.serviceType.description}</span>
                                            <span class="service-date"><fmt:formatDate value="${service.serviceDate}" pattern="MMM dd, yyyy" /></span>
                                        </div>
                                        <span class="service-car">${service.carId}</span>
                                    </li>
                                </c:forEach>
                            </ul>
                            <a href="${pageContext.request.contextPath}/service-history" class="view-all">View All</a>
                        </c:if>
                    </div>
                </div>

                <!-- Upcoming Services Card -->
                <div class="dashboard-card highlight">
                    <div class="card-header">
                        <h3>Upcoming Services</h3>
                        <i class="fas fa-wrench"></i>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty upcomingServices || upcomingServices.size() == 0}">
                            <div class="empty-state">
                                <div class="empty-icon">
                                    <i class="fas fa-calendar-check"></i>
                                </div>
                                <p>No upcoming services scheduled</p>
                                <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">Schedule Service</a>
                            </div>
                        </c:if>
                        <c:if test="${not empty upcomingServices && upcomingServices.size() > 0}">
                            <ul class="service-list">
                                <c:forEach var="service" items="${upcomingServices}" end="4">
                                    <li>
                                        <div class="service-info">
                                            <span class="service-type">${service.serviceType.description}</span>
                                            <span class="service-date"><fmt:formatDate value="${service.nextServiceDate}" pattern="MMM dd, yyyy" /></span>
                                        </div>
                                        <span class="service-car">${service.carId}</span>
                                    </li>
                                </c:forEach>
                            </ul>
                            <a href="${pageContext.request.contextPath}/upcoming-maintenance" class="view-all">View All</a>
                        </c:if>
                    </div>
                </div>

                <!-- Alerts Card -->
                <div class="dashboard-card alert-card">
                    <div class="card-header">
                        <h3>Overdue Services</h3>
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty overdueServices || overdueServices.size() == 0}">
                            <div class="empty-state">
                                <div class="empty-icon">
                                    <i class="fas fa-check-circle"></i>
                                </div>
                                <p>No overdue services at this time</p>
                            </div>
                        </c:if>
                        <c:if test="${not empty overdueServices && overdueServices.size() > 0}">
                            <ul class="alert-list">
                                <c:forEach var="service" items="${overdueServices}" end="4">
                                    <li>
                                        <div class="alert-icon"><i class="fas fa-exclamation-circle"></i></div>
                                        <div class="alert-info">
                                            <span class="alert-title">${service.serviceType.description} Overdue</span>
                                            <span class="alert-desc">Vehicle: ${service.carId}</span>
                                            <span class="alert-date">Due: <fmt:formatDate value="${service.nextServiceDate}" pattern="MMM dd, yyyy" /></span>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/update-service?recordId=${service.recordId}" class="btn btn-sm btn-danger">Schedule</a>
                                    </li>
                                </c:forEach>
                            </ul>
                            <a href="${pageContext.request.contextPath}/upcoming-maintenance" class="view-all">View All</a>
                        </c:if>
                    </div>
                </div>
            </div>

            <!-- Recent Services Section -->
            <section class="recent-services">
                <div class="section-header">
                    <h2><i class="fas fa-history"></i> Recent Services</h2>
                    <a href="${pageContext.request.contextPath}/service-history" class="view-all">View All</a>
                </div>
                <c:if test="${empty recentServices || recentServices.size() == 0}">
                    <div class="empty-state">
                        <p>No recent service records found</p>
                        <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">Add Service Record</a>
                    </div>
                </c:if>
                <c:if test="${not empty recentServices && recentServices.size() > 0}">
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Service Date</th>
                                    <th>Vehicle</th>
                                    <th>Service Type</th>
                                    <th>Cost</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="record" items="${recentServices}" end="4">
                                    <tr>
                                        <td><fmt:formatDate value="${record.serviceDate}" pattern="MMM dd, yyyy" /></td>
                                        <td>${record.carId}</td>
                                        <td>${record.serviceType.description}</td>
                                        <td>$<fmt:formatNumber value="${record.cost}" pattern="#,##0.00" /></td>
                                        <td>
                                            <span class="status-badge ${record.completed ? 'completed' : 'pending'}">
                                                ${record.completed ? 'Completed' : 'Pending'}
                                            </span>
                                        </td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/update-service?recordId=${record.recordId}" class="action-btn edit-btn" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/delete-service?recordId=${record.recordId}" class="action-btn delete-btn" title="Delete">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </section>
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
