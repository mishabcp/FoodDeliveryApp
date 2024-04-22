package com.fda.daoimplementation;

import com.fda.dao.OrderTableDAO;
import com.fda.model.Cart;
import com.fda.model.OrderTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderTableDAOImpl implements OrderTableDAO {

    private static final String INSERT_QUERY = "INSERT INTO ordertable (UserID, MenuID, Quantity, OrderDateAndTime) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_USER_QUERY = "SELECT * FROM ordertable WHERE UserID = ?";
    private static final String SELECT_BY_MENU_QUERY = "SELECT * FROM ordertable WHERE MenuID = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM ordertable";

    private static Connection connection = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fdadb", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing database connection");
        }
    }

    public void insertOrder(OrderTable order) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setInt(2, order.getMenuId());
            preparedStatement.setInt(3, order.getQuantity());
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error inserting order into database");
        }
    }

    public List<OrderTable> getOrdersByUserId(int userId) {
        List<OrderTable> orders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_USER_QUERY)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    OrderTable order = extractOrderFromResultSet(resultSet);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving orders by user ID from database");
        }
        return orders;
    }

    public List<OrderTable> getOrdersByMenuId(int menuId) {
        List<OrderTable> orders = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_MENU_QUERY)) {
            preparedStatement.setInt(1, menuId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    OrderTable order = extractOrderFromResultSet(resultSet);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving orders by menu ID from database");
        }
        return orders;
    }

    public List<OrderTable> getAllOrders() {
        List<OrderTable> orders = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                OrderTable order = extractOrderFromResultSet(resultSet);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all orders from database");
        }
        return orders;
    }

    private OrderTable extractOrderFromResultSet(ResultSet resultSet) throws SQLException {
        OrderTable order = new OrderTable();
        order.setOrderId(resultSet.getInt("OrderID"));
        order.setUserId(resultSet.getInt("UserID"));
        order.setMenuId(resultSet.getInt("MenuID"));
        order.setQuantity(resultSet.getInt("Quantity"));
        order.setOrderDateAndTime(resultSet.getTimestamp("OrderDateAndTime"));
        return order;
    }

    public void insertOrdersFromCartEntries(List<Cart> cartEntries) {
        // Assuming Cart is a model class with getUserId(), getMenuId(), and getQuantity() methods
        String query = "INSERT INTO ordertable (UserID, MenuID, Quantity) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Cart cartEntry : cartEntries) {
                preparedStatement.setInt(1, cartEntry.getUserId());
                preparedStatement.setInt(2, cartEntry.getMenuId());
                preparedStatement.setInt(3, cartEntry.getQuantity());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error inserting orders from cart entries into database");
        }
    }
}
