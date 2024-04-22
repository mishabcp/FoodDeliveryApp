package servlets;

import com.fda.dao.UserDAO;
import com.fda.daoimplementation.UserDAOImpl;
import com.fda.model.UserDTO;
import com.fda.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/signup")
public class SignupServlet extends HttpServlet {

    private final UserDAO userDAO; // Use the interface type

    public SignupServlet() {
        this.userDAO = new UserDAOImpl(); // Initialize your DAO implementation
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Read the JSON data from the request body
        String jsonData = readJsonData(request.getReader());

        // Parse JSON data into UserDTO object
        UserDTO userData = parseJsonData(jsonData);

        // Validate data
        if (!isValid(userData)) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid data provided");
            return;
        }

        // Create a User object from the received data
        User user = new User(userData.getUsername(), userData.getPassword(), userData.getEmail());

        // Save the user to the database
        try {
            userDAO.addUser(user);
            sendSuccessResponse(response, HttpServletResponse.SC_CREATED, "User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to register user");
        }
    }

    private String readJsonData(BufferedReader reader) throws IOException {
        StringBuilder jsonData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonData.append(line);
        }
        return jsonData.toString();
    }

    private UserDTO parseJsonData(String jsonData) {
        // Implement JSON parsing logic here
        // For example, using Gson or Jackson to parse the JSON string into a UserDTO object
        return null; // Replace this with actual parsing logic
    }

    private boolean isValid(UserDTO userData) {
        // Implement actual validation logic here
        // For example, check if email is in a valid format, if username is unique, etc.
        return true; // For simplicity, returning true for now
    }

    private void sendSuccessResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println("{\"message\": \"" + message + "\"}");
        out.flush();
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println("{\"error\": \"" + message + "\"}");
        out.flush();
    }
}
