package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fda.daoimplementation.CartDAOImpl;

@WebServlet("/clearCart")
public class ClearCart extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdParam = req.getParameter("userId");
        String restaurantIdParam = req.getParameter("restaurantId");

        int userId = Integer.parseInt(userIdParam);
        int restaurantId = Integer.parseInt(restaurantIdParam);

        CartDAOImpl cartDAO = new CartDAOImpl();
        cartDAO.deleteCartEntries(userId);

        // Redirect back to the cart page or any appropriate page
        resp.sendRedirect("cart.jsp?userId=" + userId + "&restaurantId=" + restaurantId);
    }
}
