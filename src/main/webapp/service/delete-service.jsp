<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Delete Service Record</title>
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
            <h2>Delete Service Record</h2>

            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <p>${errorMessage}</p>
                </div>
            </c:if>

            <div class="confirmation-box">
                <h3>Are you sure you want to delete this service record?</h3>

                <div class="record-details">
                    <p><strong>Car ID:</strong> ${serviceRecord.carId}</p>
                    <p><strong>Service Type:</strong> ${serviceRecord.serviceType.description}</p>
                    <p><strong>Service Date:</strong> <fmt:formatDate value="${serviceRecord.serviceDate}" pattern="yyyy-MM-dd" /></p>
                    <p><strong>Mileage:</strong> <fmt:formatNumber value="${serviceRecord.mileage}" pattern="#,##0.0" /> miles</p>
                    <p><strong>Cost:</strong> $<fmt:formatNumber value="${serviceRecord.cost}" pattern="#,##0.00" /></p>
                    <p><strong>Status:</strong> ${serviceRecord.completed ? 'Completed' : 'Pending'}</p>

                    <c:if test="${not empty serviceRecord.description}">
                        <p><strong>Description:</strong> ${serviceRecord.description}</p>
                    </c:if>
                </div>

                <div class="warning-message">
                    <p>This action cannot be undone. All data related to this service record will be permanently deleted.</p>
                </div>

                <form action="${pageContext.request.contextPath}/delete-service" method="post">
                    <input type="hidden" name="recordId" value="${serviceRecord.recordId}">
                    <input type="hidden" name="confirmDelete" value="yes">

                    <div class="form-buttons">
                        <button type="submit" class="btn btn-danger">Delete Service Record</button>
                        <a href="${pageContext.request.contextPath}/service-history" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </main>

        <footer>
            <p>&copy; 2025 Car Service Tracker - All Rights Reserved</p>
        </footer>
    </div>
</body>
</html>