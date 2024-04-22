package servlets;

import com.fda.daoimplementation.CartDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/decrementQuantity")
public class DecrementQty extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        int menuId = Integer.parseInt(req.getParameter("menuId"));
        int restaurantId = Integer.parseInt(req.getParameter("restaurantId"));

        CartDAOImpl cartDAO = new CartDAOImpl();
        cartDAO.decrementQuantityByMenuId(menuId);

        // Redirect back to the cart page or any other appropriate page
        resp.sendRedirect("cart.jsp?userId=" + userId + "&restaurantId=" + restaurantId);
    }
}
