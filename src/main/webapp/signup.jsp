<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.PreparedStatement"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign Up</title>
<link
	href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css"
	rel="stylesheet">
</head>
<body class="bg-gray-100">
	<div class="flex items-center justify-center h-screen">
		<div class="bg-white rounded-lg shadow-md w-96 p-8 mx-auto">
			<h2 class="text-2xl font-semibold mb-6 text-center">Sign Up</h2>
			<form action="" method="post" class="space-y-4">
				<div class="flex flex-col space-y-1">
					<label for="username" class="font-semibold">Username:</label> <input
						type="text" id="username" name="username" required
						class="px-4 py-2 border rounded-lg focus:outline-none focus:border-blue-500">
				</div>
				<div class="flex flex-col space-y-1">
					<label for="password" class="font-semibold">Password:</label> <input
						type="password" id="password" name="password" required
						class="px-4 py-2 border rounded-lg focus:outline-none focus:border-blue-500">
				</div>
				<div class="flex flex-col space-y-1">
					<label for="email" class="font-semibold">Email:</label> <input
						type="email" id="email" name="email" required
						class="px-4 py-2 border rounded-lg focus:outline-none focus:border-blue-500">
				</div>
				<button type="submit"
					class="bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 w-full">Sign
					Up</button>
			</form>
			<p class="mt-4 text-sm text-center">
				Already have an account? <a href="login.jsp"
					class="text-blue-500 hover:underline">Login</a>
			</p>

			<!-- Embedded logic to insert user details into the database -->

			<%
			    String username = request.getParameter("username");
			    String password = request.getParameter("password");
			    String email = request.getParameter("email");
			
			    if (username == null || username.isEmpty()) {
			        // Handle the case where username is null or empty
			        // You can display an error message or redirect the user back to the signup form
			        response.sendRedirect("signup.jsp?error=username_required");
			        return;
			    }
			
			    try {
			        Class.forName("com.mysql.cj.jdbc.Driver");
			        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fdadb", "root", "root");
			
			        String query = "INSERT INTO User (Username, Password, Email) VALUES (?, ?, ?)";
			        PreparedStatement preparedStatement = connection.prepareStatement(query);
			        preparedStatement.setString(1, username);
			        preparedStatement.setString(2, password);
			        preparedStatement.setString(3, email);
			
			        preparedStatement.executeUpdate();
			
			        // Redirect to login.jsp after successful signup
			        response.sendRedirect("login.jsp");
			    } catch (Exception e) {
			        e.printStackTrace();
			        // Handle other exceptions such as database connection errors
			        response.sendRedirect("signup.jsp?error=signup_failed");
			    }
			%>
		</div>
	</div>
</body>
</html>
