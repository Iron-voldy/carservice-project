<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Service Record</title>
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
                    <li><a href="${pageContext.request.contextPath}/upcoming-maintenance">Upcoming Maintenance</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile">Your Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <main>
            <h2>Update Service Record</h2>

            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <p>${errorMessage}</p>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/update-service" method="post">
                <input type="hidden" name="recordId" value="${serviceRecord.recordId}">

                <div class="form-group">
                    <label for="carId">Car ID:</label>
                    <input type="text" id="carId" name="carId" value="${serviceRecord.carId}" readonly>
                    <small>Car ID cannot be changed.</small>
                </div>

                <div class="form-group">
                    <label for="serviceType">Service Type:</label>
                    <select id="serviceType" name="serviceType" required>
                        <c:forEach items="${serviceTypes}" var="type">
                            <option value="${type.description}" ${serviceRecord.serviceType.description eq type.description ? 'selected' : ''}>
                                ${type.description}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="serviceDate">Service Date:</label>
                    <input type="date" id="serviceDate" name="serviceDate" value="<fmt:formatDate value="${serviceRecord.serviceDate}" pattern="yyyy-MM-dd" />" required>
                </div>

                <div class="form-group">
                    <label for="nextServiceDate">Next Service Date:</label>
                    <input type="date" id="nextServiceDate" name="nextServiceDate" value="<fmt:formatDate value="${serviceRecord.nextServiceDate}" pattern="yyyy-MM-dd" />">
                    <small>Leave blank to auto-calculate based on service type and mileage.</small>
                </div>

                <div class="form-group">
                    <label for="mileage">Current Mileage:</label>
                    <input type="number" id="mileage" name="mileage" step="0.1" value="${serviceRecord.mileage}" required>
                </div>

                <div class="form-group">
                    <label for="cost">Cost ($):</label>
                    <input type="number" id="cost" name="cost" step="0.01" value="${serviceRecord.cost}" required>
                </div>

                <div class="form-group">
                    <label for="description">Description/Notes:</label>
                    <textarea id="description" name="description" rows="4">${serviceRecord.description}</textarea>
                </div>

                <div class="form-group checkbox-group">
                    <input type="checkbox" id="completed" name="completed" ${serviceRecord.completed ? 'checked' : ''}>
                    <label for="completed">Mark as Completed</label>
                </div>

                <div class="form-group">
                    <label for="averageMilesPerMonth">Average Miles per Month:</label>
                    <input type="number" id="averageMilesPerMonth" name="averageMilesPerMonth" value="1000">
                    <small>Used to calculate next service date if not provided.</small>
                </div>

                <div class="form-buttons">
                    <button type="submit" class="btn btn-primary">Update Service Record</button>
                    <a href="${pageContext.request.contextPath}/service-history" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </main>

        <footer>
            <p>&copy; 2025 Car Service Tracker - All Rights Reserved</p>
        </footer>
    </div>
</body>
</html>