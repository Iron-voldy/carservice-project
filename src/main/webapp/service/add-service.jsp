<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CarCare - Add Service Record</title>
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
                <h1><i class="fas fa-plus-circle"></i> Add Service Record</h1>
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

            <!-- Add Service Form -->
            <div class="form-container">
                <form action="${pageContext.request.contextPath}/add-service" method="post" class="card-form">
                    <div class="form-section">
                        <h2>Vehicle Information</h2>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="carId">Select Vehicle:</label>
                                <select id="carId" name="carId" required>
                                    <option value="">-- Select Vehicle --</option>
                                    <option value="CAR001">Toyota Camry (2022)</option>
                                    <option value="CAR002">Honda Civic (2020)</option>
                                    <option value="CAR003">Ford F-150 (2021)</option>
                                    <option value="CAR004">Nissan Altima (2019)</option>
                                    <option value="CAR005">Chevrolet Malibu (2023)</option>
                                    <option value="CAR006">Hyundai Sonata (2021)</option>
                                    <option value="CAR007">BMW 3 Series (2022)</option>
                                    <option value="CAR008">Mercedes-Benz C-Class (2020)</option>
                                    <option value="CAR009">Audi A4 (2022)</option>
                                    <option value="CAR010">Tesla Model 3 (2023)</option>
                                </select>
                                <small>Select a vehicle from your garage</small>
                            </div>

                            <div class="form-group">
                                <label for="mileage">Current Mileage:</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-tachometer-alt"></i>
                                    <input type="number" id="mileage" name="mileage" step="0.1" min="0" required>
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
                                    <option value="">-- Select Service Type --</option>
                                    <c:forEach items="${serviceTypes}" var="type">
                                        <option value="${type.description}">${type.description}</option>
                                    </c:forEach>
                                </select>
                                <small>Type of service performed</small>
                            </div>

                            <div class="form-group">
                                <label for="serviceDate">Service Date:</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-calendar-alt"></i>
                                    <input type="date" id="serviceDate" name="serviceDate" required>
                                </div>
                                <small>Date when service was or will be performed</small>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="cost">Cost ($):</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-dollar-sign"></i>
                                    <input type="number" id="cost" name="cost" step="0.01" min="0" required>
                                </div>
                                <small>Total cost of the service</small>
                            </div>

                            <div class="form-group">
                                <label for="averageMilesPerMonth">Average Miles per Month:</label>
                                <div class="input-with-icon">
                                    <i class="fas fa-road"></i>
                                    <input type="number" id="averageMilesPerMonth" name="averageMilesPerMonth" value="1000">
                                </div>
                                <small>Used to calculate next service due date</small>
                            </div>
                        </div>

                        <div class="form-group full-width">
                            <label for="description">Service Description/Notes:</label>
                            <textarea id="description" name="description" rows="4" placeholder="Enter any additional details about the service..."></textarea>
                            <small>Describe what was done, parts replaced, or other notes</small>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Save Service Record
                        </button>
                        <a href="${pageContext.request.contextPath}/service-history" class="btn btn-outline">
                            <i class="fas fa-times"></i> Cancel
                        </a>
                    </div>
                </form>
            </div>

            <!-- Service Type Info -->
            <div class="info-section">
                <div class="info-header">
                    <h2><i class="fas fa-info-circle"></i> Service Type Information</h2>
                </div>
                <div class="service-info-grid">
                    <c:forEach items="${serviceTypes}" var="type">
                        <div class="service-info-card">
                            <h3>${type.description}</h3>
                            <p>Recommended Interval: <strong>${type.recommendedMileageInterval} miles</strong></p>
                            <p>Service includes inspection and replacement of necessary components.</p>
                        </div>
                    </c:forEach>
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

        // Show service type details when a type is selected
        document.getElementById('serviceType').addEventListener('change', function() {
            const selectedType = this.value;
            const serviceInfoCards = document.querySelectorAll('.service-info-card');

            serviceInfoCards.forEach(card => {
                if (card.querySelector('h3').textContent === selectedType) {
                    card.classList.add('highlighted');
                } else {
                    card.classList.remove('highlighted');
                }
            });
        });
    </script>
</body>
</html>