<%@ page import="java.util.List"%>
<%@ page import="com.fda.model.Cart"%>
<%@ page import="com.fda.daoimplementation.OrderTableDAOImpl"%>
<%@ page import="com.fda.daoimplementation.CartDAOImpl"%>

<%
    int userId = Integer.parseInt(request.getParameter("userId"));
    // Now you can use the userId as needed in your JSP page
%>

<!DOCTYPE html>
<html>
<head>
<style>
body {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 100vh;
	margin: 0;
	background-color: #f0f0f0;
}

.confirmation-container {
	text-align: center;
	padding: 20px;
	border: 1px solid #ccc;
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.go-to-homepage-btn {
	display: inline-block;
	margin-top: 20px;
	padding: 10px 20px;
	background-color: #3498db;
	color: #fff;
	text-decoration: none;
	border-radius: 5px;
	transition: background-color 0.3s ease;
}

.go-to-homepage-btn:hover {
	background-color: #2980b9;
}
</style>
</head>
<body>
	<div class="confirmation-container">
		<h2>Order Placed Successfully!</h2>
		<!-- Add any additional messages or details here -->

		<a href="restaurants.jsp?userId=<%=userId%>"
			class="go-to-homepage-btn">Go to Homepage</a>
	</div>
</body>
</html>
