package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fda.daoimplementation.OrderTableDAOImpl;
import com.fda.daoimplementation.CartDAOImpl;
import com.fda.model.Cart;

@WebServlet("/submitOrder")
public class SubmitOrderServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve parameters from the form
        int userId = Integer.parseInt(request.getParameter("userId"));

        try {
            
            OrderTableDAOImpl orderTableDAO = new OrderTableDAOImpl();
            CartDAOImpl cartDAO = new CartDAOImpl();


            
            List<Cart> cartEntries = cartDAO.getCartEntriesByUserId(userId);
            
            
            orderTableDAO.insertOrdersFromCartEntries(cartEntries);
            
            
            // 2. Call a method from CartDAOImpl to delete entries in the cart
            cartDAO.deleteCartEntries(userId);

            // Redirect the user to a success page or any other appropriate page
            response.sendRedirect("orderConfirmation.jsp?userId=" + userId);

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions, redirect to an error page, or show an error message
            response.sendRedirect("error.jsp");
        }
    }
}
