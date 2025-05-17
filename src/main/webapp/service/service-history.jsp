<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CarCare - Service History</title>
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
                <a href="${pageContext.request.contextPath}/service-history" class="nav-link active">
                    <i class="fas fa-car"></i> My Vehicles
                </a>
                <a href="${pageContext.request.contextPath}/upcoming-maintenance" class="nav-link">
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
                <h1><i class="fas fa-history"></i> Service History</h1>
                <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Add Service Record
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

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                    <button class="close-btn"><i class="fas fa-times"></i></button>
                </div>
            </c:if>

            <!-- Filter Section -->
            <div class="filter-section">
                <form action="${pageContext.request.contextPath}/service-history" method="get" class="filter-form">
                    <div class="filter-group">
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
                            <label for="sortBy">Sort By:</label>
                            <select id="sortBy" name="sortBy">
                                <option value="date" ${sortBy == 'date' ? 'selected' : ''}>Service Date</option>
                                <option value="nextDate" ${sortBy == 'nextDate' ? 'selected' : ''}>Next Service Date</option>
                                <option value="mileage" ${sortBy == 'mileage' ? 'selected' : ''}>Mileage</option>
                                <option value="cost" ${sortBy == 'cost' ? 'selected' : ''}>Cost</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="sortOrder">Order:</label>
                            <select id="sortOrder" name="sortOrder">
                                <option value="asc" ${sortOrder == 'asc' ? 'selected' : ''}>Ascending</option>
                                <option value="desc" ${sortOrder == 'desc' ? 'selected' : ''}>Descending</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-filter"></i> Apply Filters
                        </button>
                        <c:if test="${not empty currentCarId || sortBy != null}">
                            <a href="${pageContext.request.contextPath}/service-history" class="btn btn-outline">
                                <i class="fas fa-times"></i> Clear Filters
                            </a>
                        </c:if>
                    </div>
                </form>
            </div>

            <!-- Service History Table -->
            <div class="service-history-section">
                <c:choose>
                    <c:when test="${not empty serviceRecords && serviceRecords.size() > 0}">
                        <div class="table-responsive">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>
                                            <a href="${pageContext.request.contextPath}/service-history?sortBy=date&sortOrder=${sortBy == 'date' && sortOrder == 'asc' ? 'desc' : 'asc'}&carId=${currentCarId}" class="sort-header">
                                                Service Date
                                                <c:if test="${sortBy == 'date'}">
                                                    <i class="fas fa-sort-${sortOrder == 'asc' ? 'up' : 'down'}"></i>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="${pageContext.request.contextPath}/service-history?sortBy=nextDate&sortOrder=${sortBy == 'nextDate' && sortOrder == 'asc' ? 'desc' : 'asc'}&carId=${currentCarId}" class="sort-header">
                                                Next Service
                                                <c:if test="${sortBy == 'nextDate'}">
                                                    <i class="fas fa-sort-${sortOrder == 'asc' ? 'up' : 'down'}"></i>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>Vehicle</th>
                                        <th>Service Type</th>
                                        <th>
                                            <a href="${pageContext.request.contextPath}/service-history?sortBy=mileage&sortOrder=${sortBy == 'mileage' && sortOrder == 'asc' ? 'desc' : 'asc'}&carId=${currentCarId}" class="sort-header">
                                                Mileage
                                                <c:if test="${sortBy == 'mileage'}">
                                                    <i class="fas fa-sort-${sortOrder == 'asc' ? 'up' : 'down'}"></i>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>
                                            <a href="${pageContext.request.contextPath}/service-history?sortBy=cost&sortOrder=${sortBy == 'cost' && sortOrder == 'asc' ? 'desc' : 'asc'}&carId=${currentCarId}" class="sort-header">
                                                Cost
                                                <c:if test="${sortBy == 'cost'}">
                                                    <i class="fas fa-sort-${sortOrder == 'asc' ? 'up' : 'down'}"></i>
                                                </c:if>
                                            </a>
                                        </th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="record" items="${serviceRecords}">
                                        <tr>
                                            <td><fmt:formatDate value="${record.serviceDate}" pattern="MMM dd, yyyy" /></td>
                                            <td><fmt:formatDate value="${record.nextServiceDate}" pattern="MMM dd, yyyy" /></td>
                                            <td>${record.carId}</td>
                                            <td>${record.serviceType.description}</td>
                                            <td><fmt:formatNumber value="${record.mileage}" pattern="#,##0.0" /> mi</td>
                                            <td>$<fmt:formatNumber value="${record.cost}" pattern="#,##0.00" /></td>
                                            <td>
                                                <span class="status-badge ${record.completed ? 'completed' : 'pending'}">
                                                    ${record.completed ? 'Completed' : 'Pending'}
                                                </span>
                                            </td>
                                            <td class="actions">
                                                <div class="action-buttons">
                                                    <a href="${pageContext.request.contextPath}/update-service?recordId=${record.recordId}"
                                                       class="action-btn edit-btn" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <c:if test="${!record.completed}">
                                                        <a href="${pageContext.request.contextPath}/mark-complete?recordId=${record.recordId}&returnUrl=/service-history"
                                                           class="action-btn complete-btn" title="Mark Complete">
                                                            <i class="fas fa-check"></i>
                                                        </a>
                                                    </c:if>
                                                    <a href="${pageContext.request.contextPath}/delete-service?recordId=${record.recordId}"
                                                       class="action-btn delete-btn" title="Delete">
                                                        <i class="fas fa-trash"></i>
                                                    </a>
                                                </div>
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
                                <i class="fas fa-history"></i>
                            </div>
                            <p>No service records found.</p>
                            <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Add Service Record
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Service Statistics -->
            <div class="stats-cards">
                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="fas fa-tools"></i>
                    </div>
                    <div class="stat-content">
                        <h3>Total Services</h3>
                        <p class="stat-value">${serviceRecords.size()}</p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="fas fa-dollar-sign"></i>
                    </div>
                    <div class="stat-content">
                        <h3>Total Cost</h3>
                        <p class="stat-value">
                            $<fmt:formatNumber value="${totalCost}" pattern="#,##0.00" />
                        </p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="fas fa-tachometer-alt"></i>
                    </div>
                    <div class="stat-content">
                        <h3>Avg. Mileage</h3>
                        <p class="stat-value">
                            <fmt:formatNumber value="${avgMileage}" pattern="#,##0.0" /> mi
                        </p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon">
                        <i class="fas fa-check-circle"></i>
                    </div>
                    <div class="stat-content">
                        <h3>Completed</h3>
                        <p class="stat-value">
                            ${completedCount} <span class="stat-subtitle">of ${serviceRecords.size()}</span>
                        </p>
                    </div>
                </div>
            </div>
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