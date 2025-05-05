<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CarCare - Delete Service Record</title>
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
                <h1><i class="fas fa-trash-alt"></i> Delete Service Record</h1>
                <a href="${pageContext.request.contextPath}/service-history" class="btn btn-outline">
                    <i class="fas fa-arrow-left"></i> Back to Service History
                </a>
            </div>

            <!-- Alert Messages -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                    <button class="close-btn"><i class="fas fa-times"></i></button>
                </div>
            </c:if>

            <!-- Delete Confirmation Card -->
            <div class="confirmation-card">
                <div class="confirmation-header">
                    <div class="warning-icon">
                        <i class="fas fa-exclamation-triangle"></i>
                    </div>
                    <h2>Delete Service Record?</h2>
                </div>

                <div class="confirmation-body">
                    <p class="warning-text">This action cannot be undone. All data related to this service record will be permanently deleted.</p>

                    <div class="record-details">
                        <div class="detail-row">
                            <span class="detail-label">Vehicle:</span>
                            <span class="detail-value">${serviceRecord.carId}</span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">Service Type:</span>
                            <span class="detail-value">${serviceRecord.serviceType.description}</span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">Service Date:</span>
                            <span class="detail-value"><fmt:formatDate value="${serviceRecord.serviceDate}" pattern="MMM dd, yyyy" /></span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">Mileage:</span>
                            <span class="detail-value"><fmt:formatNumber value="${serviceRecord.mileage}" pattern="#,##0.0" /> miles</span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">Cost:</span>
                            <span class="detail-value">$<fmt:formatNumber value="${serviceRecord.cost}" pattern="#,##0.00" /></span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">Status:</span>
                            <span class="detail-value status-badge ${serviceRecord.completed ? 'completed' : 'pending'}">
                                ${serviceRecord.completed ? 'Completed' : 'Pending'}
                            </span>
                        </div>

                        <c:if test="${not empty serviceRecord.description}">
                            <div class="detail-row description-row">
                                <span class="detail-label">Description:</span>
                                <span class="detail-value">${serviceRecord.description}</span>
                            </div>
                        </c:if>
                    </div>
                </div>

                <div class="confirmation-actions">
                    <form action="${pageContext.request.contextPath}/delete-service" method="post">
                        <input type="hidden" name="recordId" value="${serviceRecord.recordId}">
                        <input type="hidden" name="confirmDelete" value="yes">

                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash"></i> Delete Service Record
                        </button>
                        <a href="${pageContext.request.contextPath}/service-history" class="btn btn-outline">
                            <i class="fas fa-times"></i> Cancel
                        </a>
                    </form>
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