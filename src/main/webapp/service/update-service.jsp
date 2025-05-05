<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CarCare - Update Service Record</title>
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
                <h1><i class="fas fa-edit"></i> Update Service Record</h1>
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

            <!-- Update Service Form -->
            <div class="form-container">
                <form action="${pageContext.request.contextPath}/update-service" method="post" class="card-form">
                    <input type="hidden" name="recordId" value="${serviceRecord.recordId}">

                    <div class="form-section">
                        <h2>Vehicle Information</h2>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="carId">Vehicle:</label>
                                <input type="text" id="carId" name="carId" value="${serviceRecord.carId}" readonly class="readonly-field">
                                <small>Vehicle ID cannot be changed</small>
                            </div>

                            <div class="form-group">
                                <label for="mileage">Current Mileage:</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-tachometer-alt"></i>
                                    <input type="number" id="mileage" name="mileage" step="0.1" value="${serviceRecord.mileage}" min="0" required>
                                </div>
                                <small>Current odometer reading in miles</small>
                            </div>
                        </div>
                    </div>

                    <div class="form-section">
                        <h2>Service Details</h2>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="serviceType">Service Type:</label>
                                <select id="serviceType" name="serviceType" required>
                                    <c:forEach items="${serviceTypes}" var="type">
                                        <option value="${type.description}" ${serviceRecord.serviceType.description eq type.description ? 'selected' : ''}>
                                            ${type.description}
                                        </option>
                                    </c:forEach>
                                </select>
                                <small>Type of service performed</small>
                            </div>

                            <div class="form-group">
                                <label for="serviceDate">Service Date:</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-calendar-alt"></i>
                                    <input type="date" id="serviceDate" name="serviceDate" value="<fmt:formatDate value="${serviceRecord.serviceDate}" pattern="yyyy-MM-dd" />" required>
                                </div>
                                <small>Date when service was performed</small>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="nextServiceDate">Next Service Date:</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-calendar-plus"></i>
                                    <input type="date" id="nextServiceDate" name="nextServiceDate" value="<fmt:formatDate value="${serviceRecord.nextServiceDate}" pattern="yyyy-MM-dd" />">
                                </div>
                                <small>Leave blank to auto-calculate</small>
                            </div>

                            <div class="form-group">
                                <label for="cost">Cost ($):</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-dollar-sign"></i>
                                    <input type="number" id="cost" name="cost" step="0.01" value="${serviceRecord.cost}" min="0" required>
                                </div>
                                <small>Total cost of the service</small>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="averageMilesPerMonth">Average Miles per Month:</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-road"></i>
                                    <input type="number" id="averageMilesPerMonth" name="averageMilesPerMonth" value="1000">
                                </div>
                                <small>Used if recalculating next service date</small>
                            </div>

                            <div class="form-group checkbox-group">
                                <label class="checkbox-container">
                                    <input type="checkbox" id="completed" name="completed" ${serviceRecord.completed ? 'checked' : ''}>
                                    <span class="checkmark"></span>
                                    Mark as Completed
                                </label>
                                <small>Check if service has been completed</small>
                            </div>
                        </div>

                        <div class="form-group full-width">
                            <label for="description">Service Description/Notes:</label>
                            <textarea id="description" name="description" rows="4">${serviceRecord.description}</textarea>
                            <small>Describe what was done, parts replaced, or other notes</small>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Update Service Record
                        </button>
                        <a href="${pageContext.request.contextPath}/service-history" class="btn btn-outline">
                            <i class="fas fa-times"></i> Cancel
                        </a>
                    </div>
                </form>
            </div>

            <!-- Service History Card -->
            <div class="info-card">
                <div class="info-card-header">
                    <h2><i class="fas fa-history"></i> Previous Service History for this Vehicle</h2>
                </div>
                <div class="info-card-body">
                    <c:choose>
                        <c:when test="${not empty vehicleHistory && vehicleHistory.size() > 0}">
                            <div class="mini-timeline">
                                <c:forEach var="record" items="${vehicleHistory}" end="4">
                                    <div class="timeline-item">
                                        <div class="timeline-marker ${record.completed ? 'completed' : 'pending'}"></div>
                                        <div class="timeline-content">
                                            <h4><fmt:formatDate value="${record.serviceDate}" pattern="MMM dd, yyyy" /> - ${record.serviceType.description}</h4>
                                            <p>Mileage: <fmt:formatNumber value="${record.mileage}" pattern="#,##0.0" /> mi</p>
                                            <p>Cost: $<fmt:formatNumber value="${record.cost}" pattern="#,##0.00" /></p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <p>No previous service records found for this vehicle.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
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

        // Toggle next service date field based on completed status
        document.getElementById('completed').addEventListener('change', function() {
            const nextServiceDateField = document.getElementById('nextServiceDate');
            const nextServiceDateLabel = document.querySelector('label[for="nextServiceDate"]');

            if (this.checked) {
                nextServiceDateField.disabled = false;
                nextServiceDateLabel.classList.remove('disabled');
            } else {
                nextServiceDateField.disabled = false;
                nextServiceDateLabel.classList.remove('disabled');
            }
        });
    </script>
</body>
</html>