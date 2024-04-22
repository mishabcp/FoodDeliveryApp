package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fda.daoimplementation.CartDAOImpl;

@WebServlet("/addToCart")
public class AddToCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve parameters from the request
        int userId = Integer.parseInt(request.getParameter("userId"));
        int menuId = Integer.parseInt(request.getParameter("menuId"));
        		
        CartDAOImpl c = new CartDAOImpl();
        c.addItemToCart(userId,menuId);

        // Perform any necessary server-side logic (e.g., add to the database)

        // For demonstration purposes, we'll just send a response back to the client
        String responseData = "Item added to cart!"; // You can customize this response

        // Set content type and write the response data
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(responseData);
        out.flush();
    }
}
