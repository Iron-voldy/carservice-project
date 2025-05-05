<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Service History</title>
    <link rel="stylesheet" href="../css/styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Car Service Tracker</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/service-history" class="active">Service History</a></li>
                    <li><a href="${pageContext.request.contextPath}/upcoming-maintenance">Upcoming Maintenance</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile">Your Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <main>
            <h2>Service History</h2>

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

            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <p>${errorMessage}</p>
                </div>
            </c:if>

            <div class="service-actions">
                <a href="${pageContext.request.contextPath}/add-service" class="btn btn-primary">Add New Service Record</a>

                <form action="${pageContext.request.contextPath}/service-history" method="get" class="filter-form">
                    <label for="carId">Filter by Car:</label>
                    <input type="text" id="carId" name="carId" value="${currentCarId}">
                    <button type="submit" class="btn btn-secondary">Filter</button>
                    <c:if test="${not empty currentCarId}">
                        <a href="${pageContext.request.contextPath}/service-history" class="btn btn-link">Clear Filter</a>
                    </c:if>
                </form>
            </div>

            <c:choose>
                <c:when test="${not empty serviceRecords && serviceRecords.size() > 0}">
                    <table class="service-table">
                        <thead>
                            <tr>
                                <th>
                                    <a href="${pageContext.request.contextPath}/service-history?sortBy=date&sortOrder=${sortBy == 'date' && sortOrder == 'asc' ? 'desc' : 'asc'}&carId=${currentCarId}">
                                        Service Date
                                        <c:if test="${sortBy == 'date'}">
                                            <span class="sort-indicator">${sortOrder == 'asc' ? '▲' : '▼'}</span>
                                        </c:if>
                                    </a>
                                </th>
                                <th>
                                    <a href="${pageContext.request.contextPath}/service-history?sortBy=nextDate&sortOrder=${sortBy == 'nextDate' && sortOrder == 'asc' ? 'desc' : 'asc'}&carId=${currentCarId}">
                                        Next Service Date
                                        <c:if test="${sortBy == 'nextDate'}">
                                            <span class="sort-indicator">${sortOrder == 'asc' ? '▲' : '▼'}</span>
                                        </c:if>
                                    </a>
                                </th>
                                <th>Car ID</th>
                                <th>Service Type</th>
                                <th>
                                    <a href="${pageContext.request.contextPath}/service-history?sortBy=mileage&sortOrder=${sortBy == 'mileage' && sortOrder == 'asc' ? 'desc' : 'asc'}&carId=${currentCarId}">
                                        Mileage
                                        <c:if test="${sortBy == 'mileage'}">
                                            <span class="sort-indicator">${sortOrder == 'asc' ? '▲' : '▼'}</span>
                                        </c:if>
                                    </a>
                                </th>
                                <th>
                                    <a href="${pageContext.request.contextPath}/service-history?sortBy=cost&sortOrder=${sortBy == 'cost' && sortOrder == 'asc' ? 'desc' : 'asc'}&carId=${currentCarId}">
                                        Cost
                                        <c:if test="${sortBy == 'cost'}">
                                            <span class="sort-indicator">${sortOrder == 'asc' ? '▲' : '▼'}</span>
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
                                    <td><fmt:formatDate value="${record.serviceDate}" pattern="yyyy-MM-dd" /></td>
                                    <td><fmt:formatDate value="${record.nextServiceDate}" pattern="yyyy-MM-dd" /></td>
                                    <td>${record.carId}</td>
                                    <td>${record.serviceType.description}</td>
                                    <td><fmt:formatNumber value="${record.mileage}" pattern="#,##0.0" /> miles</td>
                                    <td>$<fmt:formatNumber value="${record.cost}" pattern="#,##0.00" /></td>
                                    <td>${record.completed ? 'Completed' : 'Pending'}</td>
                                    <td class="actions">
                                        <a href="${pageContext.request.contextPath}/update-service?recordId=${record.recordId}" class="btn btn-sm btn-primary">Edit</a>
                                        <a href="${pageContext.request.contextPath}/delete-service?recordId=${record.recordId}" class="btn btn-sm btn-danger">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="no-records">
                        <p>No service records found.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>

        <footer>
            <p>&copy; 2025 Car Service Tracker - All Rights Reserved</p>
        </footer>
    </div>
</body>
</html>