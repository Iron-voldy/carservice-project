<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Car Service and Maintenance Tracker</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
    <div class="container">
        <header>
            <h1>Car Service and Maintenance Tracker</h1>
            <nav>
                <ul>
                    <li><a href="<c:url value='/index.jsp'/>" class="active">Home</a></li>
                    <li><a href="<c:url value='/service-history'/>">Service History</a></li>
                    <li><a href="<c:url value='/upcoming-maintenance'/>">Upcoming Maintenance</a></li>
                    <li><a href="<c:url value='/profile'/>">Your Profile</a></li>
                    <c:choose>
                        <c:when test="${empty sessionScope.userId}">
                            <li><a href="<c:url value='/login'/>">Login</a></li>
                            <li><a href="<c:url value='/register'/>">Register</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="<c:url value='/logout'/>">Logout</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </nav>
        </header>

        <main>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="success-message">
                    <p>${sessionScope.successMessage}</p>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="error-message">
                    <p>${sessionScope.errorMessage}</p>
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>

            <section class="welcome-section">
                <div class="welcome-content">
                    <h2>Welcome to Car Service and Maintenance Tracker</h2>
                    <p>Keep your vehicles in top condition by tracking all your service and maintenance records in one place.</p>

                    <c:choose>
                        <c:when test="${empty sessionScope.userId}">
                            <div class="cta-buttons">
                                <a href="<c:url value='/login'/>" class="btn btn-primary">Login</a>
                                <a href="<c:url value='/register'/>" class="btn btn-secondary">Register</a>
                            </div>
                            <p class="login-prompt">Login or register to start tracking your vehicle maintenance.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="quick-links">
                                <a href="<c:url value='/add-service'/>" class="btn btn-primary">Add New Service Record</a>
                                <a href="<c:url value='/upcoming-maintenance'/>" class="btn btn-secondary">View Upcoming Maintenance</a>
                                <a href="<c:url value='/service-history'/>" class="btn btn-secondary">View Service History</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="welcome-image">
                    <img src="<c:url value='/images/car-maintenance.jpg'/>" alt="Car Maintenance Illustration" onerror="this.style.display='none'">
                </div>
            </section>

            <c:if test="${not empty sessionScope.userId}">
                <section class="dashboard-section">
                    <h2>Your Maintenance Dashboard</h2>

                    <div class="dashboard-cards">
                        <jsp:include page="/upcoming-maintenance?component=dashboard" />

                        <div class="dashboard-card">
                            <h3>Recent Services</h3>
                            <c:set var="recentServicesCount" value="0" />

                            <c:if test="${not empty recentServices && recentServices.size() > 0}">
                                <ul class="recent-services-list">
                                    <c:forEach var="record" items="${recentServices}" end="4">
                                        <c:set var="recentServicesCount" value="${recentServicesCount + 1}" />
                                        <li>
                                            <span class="service-date"><fmt:formatDate value="${record.serviceDate}" pattern="MM/dd/yyyy" /></span>
                                            <span class="service-type">${record.serviceType.description}</span>
                                            <span class="service-car">Car: ${record.carId}</span>
                                        </li>
                                    </c:forEach>
                                </ul>

                                <c:if test="${recentServicesCount == 0}">
                                    <p class="no-data">No recent service records found.</p>
                                </c:if>

                                <a href="<c:url value='/service-history'/>" class="view-all-link">View All Service Records</a>
                            </c:if>
                        </div>

                        <div class="dashboard-card">
                            <h3>Maintenance Statistics</h3>
                            <div class="stats-container">
                                <div class="stat-item">
                                    <span class="stat-value">${serviceCount}</span>
                                    <span class="stat-label">Total Services</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-value">${overdueCount}</span>
                                    <span class="stat-label">Overdue Services</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-value">${upcomingCount}</span>
                                    <span class="stat-label">Upcoming Services</span>
                                </div>
                                <div class="stat-item">
                                    <span class="stat-value">$${totalCost}</span>
                                    <span class="stat-label">Total Spent</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </c:if>

            <section class="features-section">
                <h2>Key Features</h2>

                <div class="feature-cards">
                    <div class="feature-card">
                        <div class="feature-icon">📅</div>
                        <h3>Service Reminders</h3>
                        <p>Never miss a service with automated reminders based on date or mileage.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">📊</div>
                        <h3>Service History</h3>
                        <p>Track all maintenance records and service history for each of your vehicles.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">💰</div>
                        <h3>Cost Tracking</h3>
                        <p>Keep track of all maintenance costs and analyze your spending over time.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">📱</div>
                        <h3>Mobile Friendly</h3>
                        <p>Access your maintenance records anytime, anywhere on any device.</p>
                    </div>
                </div>
            </section>
        </main>

        <footer>
            <div class="footer-content">
                <div class="footer-section">
                    <h3>Car Service Tracker</h3>
                    <p>Keeping your vehicles in top condition</p>
                </div>

                <div class="footer-section">
                    <h3>Quick Links</h3>
                    <ul>
                        <li><a href="<c:url value='/index.jsp'/>">Home</a></li>
                        <li><a href="<c:url value='/service-history'/>">Service History</a></li>
                        <li><a href="<c:url value='/upcoming-maintenance'/>">Upcoming Maintenance</a></li>
                    </ul>
                </div>

                <div class="footer-section">
                    <h3>Contact Us</h3>
                    <p>Email: support@carservicetracker.com</p>
                    <p>Phone: (123) 456-7890</p>
                </div>
            </div>

            <div class="footer-bottom">
                <p>&copy; 2025 Car Service and Maintenance Tracker - All Rights Reserved</p>
            </div>
        </footer>
    </div>

    <script src="<c:url value='/js/scripts.js'/>"></script>
</body>
</html>