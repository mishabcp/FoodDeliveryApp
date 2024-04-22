<%@ page import="java.io.*,java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.fda.daoimplementation.RestaurantDAOImpl"%>
<%@ page import="com.fda.daoimplementation.MenuDAOImpl"%>
<%@ page import="com.fda.model.Restaurant"%>
<%@ page import="com.fda.model.Menu"%>
<%@ page import="com.fda.daoimplementation.CartDAOImpl"%>
<%@ page import="com.fda.model.Cart"%>

<%
    // Retrieve userId from session
    Integer userId = (Integer) session.getAttribute("userId");
    // Retrieve restaurantId from request parameters
    String restaurantId = request.getParameter("restaurantId");
    // Convert restaurantId to integer
    int rId = Integer.parseInt(restaurantId);
    // Set restaurantId in the session
    session.setAttribute("restaurantId", rId);

    RestaurantDAOImpl restaurantDAO = new RestaurantDAOImpl();
    Restaurant r = restaurantDAO.getRestaurantById(rId);
    MenuDAOImpl m = new MenuDAOImpl();
    List<Menu> menu_list = m.getMenusByRestaurantId(rId);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Menu</title>
<link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
<style>
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

    <div id="custom-popup-overlay" class="fixed top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center hidden">
        <div id="custom-popup" class="bg-white p-4 rounded-lg shadow-lg">
            <p>Item added to cart!</p>
        </div>
    </div>
    <div class="container w-4/5 lg:w-3/5 mx-auto">
        <div class="bg-white rounded-lg shadow-lg overflow-hidden flex p-4 mt-10">
            <div class="w-1/2 flex items-end justify-end">
                <img src="<%= r.getImagePath() %>" alt="<%= r.getName() %>" class="rounded-3xl">
            </div>
            <div class="w-1/2 flex flex-col items-center justify-center">
                <h3 class="text-3xl font-bold mb-4"><%= r.getName() %></h3>
                <p class="text-xl font-semibold text-gray-700 mb-2">
                    Cuisine Type: <%= r.getCuisineType() %>
                </p>
                <p class="text-xl font-semibold text-gray-700 mb-2">
                    Address: <%= r.getAddress() %>
                </p>
                <p class="text-xl font-semibold text-gray-700 mb-2">
                    Rating: <%= r.getRating() %>
                </p>
                <p class="text-xl font-semibold text-gray-700 mb-2">
                    Active: <%= r.isActive() %>
                </p>
            </div>
        </div>
        <hr class="my-8">
        <% for (Menu menu : menu_list) { %>
        <div class="bg-white rounded-lg shadow-lg overflow-hidden flex items-center justify-center mb-8">
            <div class="w-1/3">
                <img src="<%= menu.getImagePath() %>" alt="<%= menu.getItemName() %>" class="">
            </div>
            <div class="flex justify-center w-2/3 p-4">
                <div class="w-2/3">
                    <h3 class="text-lg font-bold mb-3"><%= menu.getItemName() %></h3>
                    <p class="mb-2">
                        <%= menu.getDescription() %>
                    </p>
                    <p class="font-bold text-red-500 mb-6">
                       $<%= menu.getPrice() %>
                    </p>
                    <form method="post" class="mr-8">
                        <input type="hidden" name="userId" value="<%= userId %>">
                        <input type="hidden" name="menuId" value="<%= menu.getMenuID() %>">
                        <button type="button" class="bg-green-500 text-white py-2 px-4 rounded-lg hover:bg-green-600 transition font-bold duration-300" onclick="addToCart(<%=userId %>, '<%= menu.getMenuID() %>')">Add 1 Item to Cart</button>
                    </form>

                </div>
            </div>
        </div>
        <% } %>
        <form action="cart.jsp" method="get" class="mb-20 mt-20 text-right">
            <input type="hidden" name="userId" value="<%= userId %>">
            <input type="hidden" name="restaurantId" value="<%= restaurantId %>"> <!-- Update value to rId -->
            <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-600 font-bold transition duration-300">Go to Cart</button>
        </form>

    </div>
    <script>
        function showPopup(data) {
            var customPopupOverlay = document.getElementById("custom-popup-overlay");
            var customPopup = document.getElementById("custom-popup");
            customPopupOverlay.classList.remove("hidden");
            customPopup.classList.remove("hidden");
            setTimeout(function () {
                customPopupOverlay.classList.add("hidden");
                customPopup.classList.add("hidden");
            }, 500); // 1/2 seconds
        }

        function addToCart(userId, menuId) {
            fetch('addToCart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'userId=' + userId + '&menuId=' + menuId,
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(data => {
                showPopup(data);
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
        }
        
        function toggleDropdown() {
            var dropdownMenu = document.getElementById("dropdownMenu");
            dropdownMenu.style.display = dropdownMenu.style.display === "block" ? "none" : "block";
        }
    </script>
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
</body>
</html>
