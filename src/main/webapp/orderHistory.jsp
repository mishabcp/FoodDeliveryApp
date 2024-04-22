<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fda.daoimplementation.OrderTableDAOImpl" %>
<%@ page import="com.fda.model.OrderTable" %> 
<%@ page import="com.fda.model.Restaurant" %> 
<%@ page import="com.fda.model.Menu" %> 
<%@ page import="java.util.List" %>
<%@ page import="com.fda.daoimplementation.MenuDAOImpl" %>
<%@ page import="com.fda.daoimplementation.RestaurantDAOImpl" %>
<%@ page import="java.text.DecimalFormat" %>
<%! DecimalFormat df = new DecimalFormat("#0.00"); %>

    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order History</title>
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<style>
    body {
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }
    footer {
        margin-top: auto;
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
<body>
	<%
		Integer userId = (Integer) session.getAttribute("userId");
	%>
	<%! 
	 public String getOrderMenuImage(OrderTable order) {
        // Fetch the MenuID from the OrderTable
        int menuId = order.getMenuId();

        // Use DAO classes to fetch menu details by MenuID
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        Menu menu = menuDAO.getMenuById(menuId);

        // Return the image URL from the Menu object
        return menu.getImagePath();
    }

    public String getOrderRestaurantName(OrderTable order) {
        // Fetch the MenuID from the OrderTable
        int menuId = order.getMenuId();

        // Use DAO classes to fetch menu details by MenuID
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        Menu menu = menuDAO.getMenuById(menuId);

        // Fetch the RestaurantID from the Menu object
        int restaurantId = menu.getRestaurantID();

        // Use DAO classes to fetch restaurant details by RestaurantID
        RestaurantDAOImpl restaurantDAO = new RestaurantDAOImpl();
        Restaurant restaurant = restaurantDAO.getRestaurantById(restaurantId);

        // Return the restaurant name from the Restaurant object
        return restaurant.getName();
    }

    public String getOrderMenuName(OrderTable order) {
        // Fetch the MenuID from the OrderTable
        int menuId = order.getMenuId();

        // Use DAO classes to fetch menu details by MenuID
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        Menu menu = menuDAO.getMenuById(menuId);

        // Return the menu name from the Menu object
        return menu.getItemName();
    }
    public double getTotalPrice(OrderTable order) {
        // Fetch the MenuID from the OrderTable
        int menuId = order.getMenuId();

        // Use DAO classes to fetch menu details by MenuID
        MenuDAOImpl menuDAO = new MenuDAOImpl();
        Menu menu = menuDAO.getMenuById(menuId);

        // Calculate total price based on quantity and actual price
        double totalPrice = order.getQuantity() * menu.getPrice();

        return totalPrice;
    }
    
%>
	<header class="bg-red-500 text-white py-6">
	    <div class="container w-4/5 mx-auto flex justify-between items-center">
	        <div class="flex items-center">
	            <a href="restaurants.jsp?userId=<%= userId %>" class="flex items-center">
	                <img src="images/logo.jpg" alt="Logo" class="h-14 rounded-full">
	                <h2 class="text-2xl font-bold ml-2 text-2xl">SwiftEats</h2>
	            </a>
	        </div>
	        <div class="md:flex space-x-16 relative">
	            <a href="restaurants.jsp?userId=<%= userId %>" class="font-bold text-xl">Home</a>
	            <% if (userId == null) { %>
	                <a href="login.jsp" class="font-bold text-xl">Login</a>
	                <a href="signup.jsp" class="font-bold text-xl">SignUp</a>
	            <% } %>
	            <a href="cart.jsp?userId=<%= userId %>&restaurantId=10" class="text-white text-xl font-bold">Cart</a>
	            <div class="relative inline-block">
	                <img src="images/user_logo.jpg" alt="User Logo" class="h-8 w-8 ml-4 rounded-full cursor-pointer" onclick="toggleDropdown()">
	                <div id="dropdownMenu" class="dropdown-menu">
	                    <a href="login.jsp">Sign Out</a>
	                </div>
	            </div>
	        </div>
	    </div>
	</header>
	<div class="container lg:w-3/5 w-4/5 mb-20 mx-auto">
	    <h1 class="text-4xl text-center font-bold my-16">Order <span class=" text-red-500">History</span></h1>
	   <div class="grid grid-cols-1 gap-4">
		    <% 
		        // Fetch order history from the database
		        OrderTableDAOImpl orderTableDAO = new OrderTableDAOImpl();
		        List<OrderTable> orderHistory = orderTableDAO.getOrdersByUserId(userId);
		
		        // Iterate over the order history and display each order
		        for (OrderTable order : orderHistory) { 
		    %>
		    <div class="bg-white flex shadow-md rounded-xl"> <!-- Added mb-4 for margin bottom -->
		        <div class="w-1/4 mr-8">
		        	<img src="<%= getOrderMenuImage(order) %>" alt="Menu Image" class="rounded-xl">
		        </div>
		        <div class="w-2/4 flex flex-col justify-center">
		        	<p class="font-semibold text-lg mb-2"><%= getOrderRestaurantName(order) %></p>
			        <div class="flex  mb-2">
			        	<p class="mr-4 text-lg font-semibold"><%= getOrderMenuName(order) %></p>
			        	<p class="bg-red-500 px-1 font-bold text-white rounded-md">x<%= order.getQuantity() %></p>
			        </div>
			        <p class="font-semibold"><%= order.getOrderDateAndTime() %></p>
		        </div>
		        <div class="w-1/4 flex flex-col justify-end items-center mr-4 justify-center">
			        <p class="text-red-500 text-lg font-bold">$<%= df.format(getTotalPrice(order)) %></p>
			        <form action="orderAgain" method="post">
					    <input type="hidden" name="userId" value="<%= userId %>">
					    <input type="hidden" name="restaurantId" value="10">
					    <input type="hidden" name="menuId" value="<%= order.getMenuId() %>">
					    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded-md mt-4">Order Again</button>
					</form>
		        </div>
		    </div>
		    <% } %>
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