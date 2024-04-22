<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fda.daoimplementation.RestaurantDAOImpl" %>
<%@ page import="com.fda.model.Restaurant" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Restaurants</title>
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
<style>
    html,
    body {
        height: 100%;
    }
    body {
        display: flex;
        flex-direction: column;
    }
    .wrapper {
        flex: 1;
    }
    .dropdown-menu {
        display: none;
        position: absolute;
        background-color: #fff;
        min-width: 120px;
        box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
        z-index: 1;
    }
    .dropdown-menu a {
        display: block;
        padding: 12px 16px;
        text-decoration: none;
        color: #000;
    }
    .dropdown-menu a:hover {
        background-color: #f9f9f9;
    }
</style>
</head>
<body class="bg-gray-100">
<%
    HttpSession session2 = request.getSession();
    // Retrieve userId from the session
    Integer userId = (Integer) session2.getAttribute("userId");
%>
<header class="bg-red-500 text-white py-6">
    <div class="container w-4/5 mx-auto flex justify-between items-center">
        <div class="flex items-center">
            <a href="restaurants.jsp?userId=<%= userId %>" class="flex items-center">
                <!-- Apply rounded style to the logo image -->
                <img src="images/logo.jpg" alt="Logo" class="h-14 rounded-full">
                <!-- Apply white and semi-bold styles to the text -->
                <h2 class="text-2xl font-bold ml-2 text-2xl">SwiftEats</h2>
            </a>
        </div>
        <div class="md:flex space-x-16 relative">
            <!-- Apply white and semi-bold styles to the home link -->
            <a href="restaurants.jsp?userId=<%= userId %>" class="font-bold text-xl">Home</a>
            
            <!-- Display Login and SignUp links only if userId is not null -->
            <% if (userId == null) { %>
                <a href="login.jsp" class="font-bold text-xl">Login</a>
                <a href="signup.jsp" class="font-bold text-xl">SignUp</a>
            <% } %>
            <!-- Cart link with parameters -->
            <a href="cart.jsp?userId=<%= userId %>&restaurantId=10" class="text-white text-xl font-bold">Cart</a>
            <a href="orderHistory.jsp?userId=<%= userId %>" class="text-white text-xl font-bold">Orders</a>
            <!-- User logo with dropdown menu -->
            <div class="relative inline-block">
                <img src="images/user_logo.jpg" alt="User Logo" class="h-8 w-8 ml-4 rounded-full cursor-pointer" onclick="toggleDropdown()">
                <!-- Dropdown menu -->
                <div id="dropdownMenu" class="dropdown-menu">
                    <a href="login.jsp">Sign Out</a>
                </div>
            </div>
        </div>
    </div>
</header>

<div class="container w-4/5 lg:w-3/5 mx-auto pt-16">
    <p class="text-3xl text-red font-bold mb-4">Welcome to <span class="text-red-500 text-5xl text-bold"> SwiftEats </span> Your cravings, delivered</p>
</div>

<!-- Search Bar -->
<div class="container w-4/5 lg:w-3/5 mx-auto py-8">
	<div class="w-2/5">
	    <div class="flex w-full mb-8">
	        <input type="text" id="searchInput" placeholder="Search restaurants..." class="py-1 px-3 rounded-md border w-full">
	        <button class="ml-2 py-1 px-4 bg-red-500 text-white rounded-md hover:bg-red-600">Search</button>
	    </div>
	</div>
</div>


<div class="wrapper">
    <div class="container w-4/5 lg:w-3/5 mb-20 mx-auto grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8">
        <!-- Assuming you have a method to get restaurants from the database -->
        <%
            RestaurantDAOImpl restaurantDAO = new RestaurantDAOImpl();
            // Call the method to get sample restaurants
            List<Restaurant> restaurants = restaurantDAO.getSampleRestaurantsFromDatabase();
            for (Restaurant restaurant : restaurants) {
        %>
        <a href="menu.jsp?restaurantId=<%= restaurant.getRestaurantID() %>&userId=<%= userId %>"
            class="block rounded-xl overflow-hidden shadow-md hover:shadow-lg p-2 bg-white text-red-500">
            <img src="<%= restaurant.getImagePath() %>" alt="<%= restaurant.getName() %>" class="w-full h-48 rounded-xl object-cover">
            <div class="p-4">
                <h3 class="text-lg font-bold mb-2"><%= restaurant.getName() %></h3>
                <div class="w-1/4 flex justify-center items-center">
	                <div class="w-full mx-auto m-1 bg-green-500 rounded-lg">
	                	<p class="text-white pl-2 text-bold text-lg">&#9733; <%= restaurant.getRating() %></p>
	                </div>
                </div>
            </div>
        </a>
        <%
            }
        %>
    </div>
</div>

<footer class="bg-red-500 text-white py-4 text-center">
    <div class="container mx-auto flex flex-col md:flex-row items-center justify-between">
        <div class="mb-4 md:mb-0">© 2024 Your Restaurant App</div>
        <div class="flex items-center space-x-4">
            <a href="#" class="">Privacy Policy</a> 
            <a href="#" class="">Terms of Service</a> 
            <a href="#" class="">Contact Us</a>
        </div>
        <div class="flex items-center space-x-4 mt-4 md:mt-0">
            <a href="#" class="text-gray-300">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 11a4 4 0 1 1-8 0 4 4 0 0 1 8 0zm0 0h0m0 0zm0 0h0m0 0zm0 0h0m0 0zm0 0h0m0 0zM8 11a4 4 0 1 1 8 0 4 4 0 0 1-8 0zm0 0h0m0 0zm0 0h0m0 0zm0 0h0m0 0zm0 0h0m0 0z" />
                </svg>
            </a> 
            <a href="#" class="text-gray-300">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0zm0 0h0m0 0zm0 0h0m0 0zm0 0h0m0 0zm0 0h0m0 0zM3 19a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2V8a2 2 0 0 1-2 2h-4l-3 3-3-3H5a2 2 0 0 1-2-2zm0 0h0m0 0zm0 0h0m0 0zm0 0h0m0 0zm0 0h0m0 0z" />
                </svg>
            </a> 
            <a href="#" class="text-gray-300">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
                </svg>
            </a>
        </div>
    </div>
</footer>


<script>
    function toggleDropdown() {
        var dropdownMenu = document.getElementById("dropdownMenu");
        dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
    }

</script>

</body>
</html>
