<%@ page import="java.sql.*"%>
<%@ page import="com.fda.daoimplementation.UserDAOImpl"%>
<%@ page import="com.fda.model.User"%>
<%@ page import="javax.servlet.http.HttpSession"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login Page</title>
<!-- Include Tailwind CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css"
	rel="stylesheet">
</head>
<body class="bg-gray-100">

	<div class="flex items-center justify-center h-screen">
		<div class="bg-white rounded-lg shadow-md w-96 p-8">
			<h2 class="text-2xl font-semibold mb-6 text-center">Login</h2>
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
				<button type="submit"
					class="bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 w-full">Login</button>
			</form>
			<p class="mt-4 text-sm text-center">
				Don't have an account? <a href="signup.jsp"
					class="text-blue-500 hover:underline">Sign up here</a>
			</p>

			<!-- Authentication Logic -->
			<%
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Check authentication logic
            boolean isAuthenticated = UserDAOImpl.authenticateUser(username, password);

            if (isAuthenticated) {
                UserDAOImpl userDAO = new UserDAOImpl();

                // Get user ID after successful login
                int userId = userDAO.getUserIdByUsername(username);

                // Store user ID in session (renamed the session variable to session1)
                HttpSession session1 = request.getSession();
                session1.setAttribute("userId", userId);

                // Redirect to homepage.jsp after successful login
                response.sendRedirect("Homepage.jsp");
            } else if (username != null || password != null) {
                // Display error message if authentication fails
                out.println("<p class='error-msg text-red-500'>Wrong username or password. Please try again.</p>");
            }
        %>
		</div>
	</div>

</body>
</html>
