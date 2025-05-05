<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Service Record</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
    <div class="container">
        <header>
            <h1>Car Service Tracker</h1>
            <nav>
                <ul>
                    <li><a href="<c:url value='/index.jsp'/>">Home</a></li>
                    <li><a href="<c:url value='/service-history'/>">Service History</a></li>
                    <li><a href="<c:url value='/upcoming-maintenance'/>">Upcoming Maintenance</a></li>
                    <li><a href="<c:url value='/profile'/>">Your Profile</a></li>
                </ul>
            </nav>
        </header>

        <main>
            <h2>Add New Service Record</h2>

            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <p>${errorMessage}</p>
                </div>
            </c:if>

            <form action="<c:url value='/add-service'/>" method="post">
                <div class="form-group">
                    <label for="carId">Car ID:</label>
                    <input type="text" id="carId" name="carId" required>
                </div>

                <div class="form-group">
                    <label for="serviceType">Service Type:</label>
                    <select id="serviceType" name="serviceType" required>
                        <c:forEach items="${serviceTypes}" var="type">
                            <option value="${type.description}">${type.description}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="serviceDate">Service Date:</label>
                    <input type="date" id="serviceDate" name="serviceDate" required>
                </div>

                <div class="form-group">
                    <label for="mileage">Current Mileage:</label>
                    <input type="number" id="mileage" name="mileage" step="0.1" required>
                </div>

                <div class="form-group">
                    <label for="cost">Cost ($):</label>
                    <input type="number" id="cost" name="cost" step="0.01" required>
                </div>

                <div class="form-group">
                    <label for="description">Description/Notes:</label>
                    <textarea id="description" name="description" rows="4"></textarea>
                </div>

                <div class="form-group">
                    <label for="averageMilesPerMonth">Average Miles per Month:</label>
                    <input type="number" id="averageMilesPerMonth" name="averageMilesPerMonth" value="1000">
                    <small>This helps us calculate your next service date based on mileage intervals.</small>
                </div>

                <div class="form-buttons">
                    <button type="submit" class="btn btn-primary">Add Service Record</button>
                    <a href="<c:url value='/service-history'/>" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </main>

        <footer>
            <p>&copy; 2025 Car Service Tracker - All Rights Reserved</p>
        </footer>
    </div>
</body>
</html>