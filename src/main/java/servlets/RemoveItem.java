package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fda.daoimplementation.CartDAOImpl;

@WebServlet("/removeCartItem")
public class RemoveItem extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdParam = req.getParameter("userId");
        String menuIdParam = req.getParameter("menuId");
        String restaurantIdParam = req.getParameter("restaurantId");

        if (userIdParam == null || userIdParam.isEmpty() || menuIdParam == null || menuIdParam.isEmpty() || restaurantIdParam == null || restaurantIdParam.isEmpty()) {
            // Handle invalid parameters
            resp.sendRedirect("error.jsp"); // Redirect to an error page
            return;
        }

        int userId = Integer.parseInt(userIdParam);
        int menuId = Integer.parseInt(menuIdParam);
        int restaurantId = Integer.parseInt(restaurantIdParam);

        CartDAOImpl cartDAO = new CartDAOImpl();
        cartDAO.removeEntryFromCart( menuId, userId);

        // Redirect back to the cart page or any appropriate page
        resp.sendRedirect("cart.jsp?userId=" + userId + "&restaurantId=" + restaurantId);
    }
}
