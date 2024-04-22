package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fda.daoimplementation.CartDAOImpl;

@WebServlet("/updateCartItem")
public class UpdateCartItem extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        int menuId = Integer.parseInt(req.getParameter("menuId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        int restaurantId = Integer.parseInt(req.getParameter("restaurantId")); // Receive restaurantId

        CartDAOImpl cartDAO = new CartDAOImpl();
        cartDAO.updateCart(menuId, userId, quantity);

        // Redirect back to the cart page with userId, restaurantId, and any other appropriate parameters
        resp.sendRedirect("cart.jsp?userId=" + userId + "&restaurantId=" + restaurantId);
    }
}
