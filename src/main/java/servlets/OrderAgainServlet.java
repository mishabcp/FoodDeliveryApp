package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fda.daoimplementation.CartDAOImpl;

@WebServlet("/orderAgain")
public class OrderAgainServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve parameters from the request
        int userId = Integer.parseInt(request.getParameter("userId"));
        int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
        int menuId = Integer.parseInt(request.getParameter("menuId"));
		
        CartDAOImpl c = new CartDAOImpl();
        c.addItemToCart(userId,menuId);
        
        response.sendRedirect("cart.jsp?userId=" + userId + "&restaurantId=" + restaurantId);
    }
}
