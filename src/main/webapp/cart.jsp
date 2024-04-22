<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.fda.daoimplementation.CartDAOImpl" %>
<%@ page import="com.fda.daoimplementation.MenuDAOImpl" %>
<%@ page import="com.fda.model.Cart" %>
<%@ page import="com.fda.model.Menu" %>

<!DOCTYPE html>
<html class="h-full">
<head>
<meta charset="UTF-8">
<title>Your Cart</title>
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
<style>
    .item-details {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: calc(100% - 25%);
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
    .wrapper {
        flex: 1;
    }
    /* Flexbox styles to stick footer to bottom */
    body {
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }
    footer {
        margin-top: auto;
    }
</style>
</head>
<body class="flex flex-col h-full">


<%    	Integer userId = (Integer) session.getAttribute("userId");
		String restaurantId = request.getParameter("restaurantId");
		int rId = 0; // Default value or handle gracefully if needed
		if (restaurantId != null) {
		    rId = Integer.parseInt(restaurantId);
		    } 
%>

<header class="bg-red-500 text-white py-6">
    <div class="container w-4/5 mx-auto flex justify-between items-center">
        <div class="flex items-center">
            <a href="restaurants.jsp" class="flex items-center">
                <!-- Apply rounded style to the logo image -->
                <img src="images/logo.jpg" alt="Logo" class="h-12 rounded-full">
                <!-- Apply white and semi-bold styles to the text -->
                <h2 class="text-xl font-semibold ml-2 text-white">SwiftEats</h2>
            </a>
        </div>
        <div class="flex-grow"></div> <!-- This will push Home and User Logo to the right -->
        <div class="flex space-x-16">
            <!-- Apply white and semi-bold styles to the home link -->
            <a href="restaurants.jsp" class="text-white text-xl font-bold">Home</a>
            <a href="orderHistory.jsp?userId=<%= userId %>" class="text-white text-xl font-bold">Orders</a>
            
            <!-- Display Login and SignUp links only if userId is not null -->
            <% if (userId == null) { %>
                <a href="login.jsp" class="text-white text-xl font-bold">Login</a>
                <a href="signup.jsp" class="text-white text-xl font-bold">SignUp</a>
            <% } %>
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



<h1 class="text-4xl text-center text-gray-900 mt-10 mb-16"><span class="font-bold text-black">Your</span> <span class="font-bold text-blue-500">Cart</span></h1>

<%
    if (userId != null) {
        try {
            CartDAOImpl cartDAO = new CartDAOImpl();
            List<Cart> cartItems = cartDAO.getCartByUserId(userId);
            MenuDAOImpl m = new MenuDAOImpl();

            double totalAmount = 0.0; // Initialize total amount variable

            // Calculate total amount based on cart items
            for (Cart item : cartItems) {
                double itemPrice = m.getMenuById(item.getMenuId()).getPrice();
                int itemQuantity = item.getQuantity();
                totalAmount += itemPrice * itemQuantity;
            }
%>

<div class="mb-10 lg:w-3/5 w-4/5 mx-auto">
    <% if (cartItems.isEmpty()) { %>
    <p class="text-center text-gray-700 mt-10">Cart Empty</p>
    <% } else { %>
    <% for (Cart item : cartItems) { %>
    <div class="bg-white flex rounded-lg shadow-md overflow-hidden mb-6">
        <img src="<%= m.getMenuById(item.getMenuId()).getImagePath() %>"
             alt="<%= m.getMenuById(item.getMenuId()).getItemName() %>"
             class="w-1/4 h-auto rounded-2xl object-cover mr-6">
        <div class="p-4 item-details">
            <h2 class="text-xl font-bold"><%= m.getMenuById(item.getMenuId()).getItemName() %></h2>
            <p class="text-xl font-bold">$<%= String.format("%.2f", m.getMenuById(item.getMenuId()).getPrice()) %></p> <!-- Display Price -->
            <div class="flex items-center">
            	<form action="decrementQuantity" method="post" onsubmit="return validateQuantity(<%= item.getQuantity() %>)">
	            	<input type="hidden" name="userId" value="<%= userId %>">
					<input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
				    <input type="hidden" name="restaurantId" value="<%= restaurantId %>">
	            	<button type="submit" class="bg-green-500 text-white px-4 py-2 rounded-md">-</button>
	            </form>
	            <p class="text-xl font-bold p-4"><%= item.getQuantity() %></p>
	            <form action="incrementQuantity" method="post">
	            	<input type="hidden" name="userId" value="<%= userId %>">
					<input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
				    <input type="hidden" name="restaurantId" value="<%= restaurantId %>">
	            	<button type="submit" class="bg-green-500 text-white px-4 py-2 rounded-md">+</button>
	            </form>
            </div>
            <form action="removeCartItem" method="post">
				<input type="hidden" name="userId" value="<%= userId %>">
				<input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
				<input type="hidden" name="restaurantId" value="<%= restaurantId %>">
				<button type="submit" class="bg-red-500 text-white px-4 py-2 rounded-md">Remove</button>
			</form>
        </div>
    </div>
    <% } %>
    <% } %>
</div>
<div class="mx-auto w-3/5">
	<% if (!cartItems.isEmpty()) { %>
<div class="flex justify-end mb-4">
    <p class="text-2xl font-bold text-gray-700">Cart Total: $<%= String.format("%.2f", totalAmount) %></p>
</div>
<div class="flex justify-center space-x-4 mb-10">
    <form action="menu.jsp" method="get">
        <input type="hidden" name="restaurantId" value="<%= rId %>">
        <button type="submit" class="bg-blue-500 font-bold text-white px-4 py-2 rounded-md">Add More</button>
    </form>
    <form action="clearCart" method="post">
        <input type="hidden" name="userId" value="<%=userId%>">
        <input type="hidden" name="restaurantId" value="<%= restaurantId %>">
        <button type="submit" class="bg-yellow-500 font-bold text-white px-4 py-2 rounded-md">Clear Cart</button>
    </form>

    <form action="submitOrder" method="post">
        <input type="hidden" name="userId" value="<%=userId%>">
        <% for (Cart item : cartItems) { %>
        <input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
        <% break; } %>
        <button type="submit" class="bg-green-500 font-bold text-white px-4 py-2 rounded-md">Submit Order</button>
    </form>
</div>
<% }else{ %>
    <div class="flex justify-center space-x-4 mb-10">
        <form action="restaurants.jsp" method="get">
            <button type="submit" class="bg-blue-500 font-bold text-white px-4 py-2 rounded-md">Add More</button>
        </form>
    </div>
<%} %>

<%
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
%>
<p class="text-center text-gray-700 mt-10">User not authenticated. Please log in.</p>
<% } %>
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
    function validateQuantity(quantity) {
        if (quantity > 1) {
            return true; // Form will submit
        } else {
            return false; // Form will not submit
        }
    }
    
    function toggleDropdown() {
        var dropdownMenu = document.getElementById("dropdownMenu");
        dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
    }
</script>
</body>
</html>
