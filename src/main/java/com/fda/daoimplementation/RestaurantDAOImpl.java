package com.fda.daoimplementation;

import com.fda.dao.RestaurantDAO;
import com.fda.model.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAOImpl implements RestaurantDAO {

    private static Connection connection = null;

    private static final String INSERT_QUERY = "INSERT INTO `Restaurant` (`Name`, `CuisineType`, `DeliveryTime`, `Address`, `AdminUserID`, `Rating`, `IsActive`, `ImagePath`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM `Restaurant` WHERE `RestaurantID` = ?";
    private static final String UPDATE_QUERY = "UPDATE `Restaurant` SET `Name` = ?, `CuisineType` = ?, `DeliveryTime` = ?, `Address` = ?, `AdminUserID` = ?, `Rating` = ?, `IsActive` = ?, `ImagePath` = ? WHERE `RestaurantID` = ?";
    private static final String SELECT_QUERY = "SELECT * FROM `Restaurant` WHERE `RestaurantID` = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM `Restaurant`";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fdadb", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing database connection");
        }
    }

    @Override
    public void addRestaurant(Restaurant restaurant) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, restaurant.getName());
            preparedStatement.setString(2, restaurant.getCuisineType());
            preparedStatement.setInt(3, restaurant.getDeliveryTime());
            preparedStatement.setString(4, restaurant.getAddress());
            preparedStatement.setInt(5, restaurant.getAdminUserID());
            preparedStatement.setDouble(6, restaurant.getRating());
            preparedStatement.setBoolean(7, restaurant.isActive());
            preparedStatement.setString(8, restaurant.getImagePath());

            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Error adding restaurant to the database");
        }
    }

    @Override
    public void deleteRestaurant(int restaurantId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, restaurantId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting restaurant from the database");
        }
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, restaurant.getName());
            preparedStatement.setString(2, restaurant.getCuisineType());
            preparedStatement.setInt(3, restaurant.getDeliveryTime());
            preparedStatement.setString(4, restaurant.getAddress());
            preparedStatement.setInt(5, restaurant.getAdminUserID());
            preparedStatement.setDouble(6, restaurant.getRating());  // Use getDouble for a double field
            preparedStatement.setBoolean(7, restaurant.isActive());
            preparedStatement.setString(8, restaurant.getImagePath());
            preparedStatement.setInt(9, restaurant.getRestaurantID());

            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Error updating restaurant in the database");
        }
    }

    @Override
    public Restaurant getRestaurantById(int restaurantId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setInt(1, restaurantId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractRestaurantFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving restaurant from the database");
        }
        return null;
    }

    @Override
    public  List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                restaurantList.add(extractRestaurantFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all restaurants from the database");
        }
        return restaurantList;
    }

    private Restaurant extractRestaurantFromResultSet(ResultSet resultSet) throws SQLException {
        int restaurantId = resultSet.getInt("RestaurantID");
        String name = resultSet.getString("Name");
        String cuisineType = resultSet.getString("CuisineType");
        int deliveryTime = resultSet.getInt("DeliveryTime");
        String address = resultSet.getString("Address");
        int adminUserId = resultSet.getInt("AdminUserID");
        double rating = resultSet.getDouble("Rating");  // Retrieve as double
        boolean isActive = resultSet.getBoolean("IsActive");
        String imagePath = resultSet.getString("ImagePath");

        return new Restaurant(restaurantId, name, cuisineType, deliveryTime, address, adminUserId, rating, isActive, imagePath);
    }
    
    // Method to get sample restaurants from the database
    public  List<Restaurant> getSampleRestaurantsFromDatabase() {
        List<Restaurant> sampleRestaurants = new ArrayList<>();

        try {
            // Fetch all restaurants from the database
            List<Restaurant> allRestaurants = getAllRestaurants();

            // Add all fetched restaurants to the sampleRestaurants list
            sampleRestaurants.addAll(allRestaurants);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception appropriately, e.g., log or throw a custom exception
        }

        return sampleRestaurants;
    }
}