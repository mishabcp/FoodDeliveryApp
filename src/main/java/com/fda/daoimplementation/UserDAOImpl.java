package com.fda.daoimplementation;

import com.fda.dao.UserDAO;
import com.fda.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static Connection connection = null;

    private static final String INSERT_QUERY = "INSERT INTO `User` (`Username`, `Password`, `Email`, `Address`, `Role`, `CreatedDate`, `LastLoginDate`) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM `User` WHERE `UserID` = ?";
    private static final String UPDATE_QUERY = "UPDATE `User` SET `Username` = ?, `Password` = ?, `Email` = ?, `Address` = ?, `Role` = ?, `CreatedDate` = ?, `LastLoginDate` = ? WHERE `UserID` = ?";
    private static final String SELECT_QUERY = "SELECT * FROM `User` WHERE `UserID` = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM `User`";

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
    public void addUser(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(user.getCreatedDate()));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(user.getLastLoginDate()));

            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Error adding user to the database");
        }
    }
    

    @Override
    public void deleteUser(int userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting user from the database");
        }
    }

    @Override
    public void updateUser(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(user.getCreatedDate()));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(user.getLastLoginDate()));
            preparedStatement.setInt(8, user.getUserID());

            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Error updating user in the database");
        }
    }

    @Override
    public User getUserById(int userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving user from the database");
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                userList.add(extractUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all users from the database");
        }
        return userList;
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("UserID");
        String username = resultSet.getString("Username");
        String password = resultSet.getString("Password");
        String email = resultSet.getString("Email");
        String address = resultSet.getString("Address");
        String role = resultSet.getString("Role");
        LocalDateTime createdDate = resultSet.getTimestamp("CreatedDate").toLocalDateTime();
        LocalDateTime lastLoginDate = resultSet.getTimestamp("LastLoginDate").toLocalDateTime();

        return new User(userId, username, password, email, address, role, createdDate, lastLoginDate);
    }

    @Override
    public User getUserByEmail(String email) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `User` WHERE `Email` = ?")) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int retrievedUserId = resultSet.getInt("UserID");
                    String username = resultSet.getString("Username");
                    String password = resultSet.getString("Password");
                    String retrievedEmail = resultSet.getString("Email");
                    String address = resultSet.getString("Address");
                    String role = resultSet.getString("Role");
                    LocalDateTime createdDate = resultSet.getTimestamp("CreatedDate").toLocalDateTime();
                    LocalDateTime lastLoginDate = resultSet.getTimestamp("LastLoginDate").toLocalDateTime();

                    return new User(retrievedUserId, username, password, retrievedEmail, address, role, createdDate, lastLoginDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving user by email from the database");
        }
        return null;
    }
    public static boolean authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fdadb", "root", "root");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `User` WHERE `Username` = ? AND `Password` = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Authentication successful, update lastLoginDate
                    int userId = resultSet.getInt("UserID");
                    updateLastLoginDate(userId);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error authenticating user against the database");
        }
        return false;
    }

    private static void updateLastLoginDate(int userId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fdadb", "root", "root");
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `User` SET `LastLoginDate` = ? WHERE `UserID` = ?")) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating lastLoginDate in the database");
        }
    }
    public int getUserIdByUsername(String username) {
        int userId = -1;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT `UserID` FROM `User` WHERE `Username` = ?")) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("UserID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting user ID by username from the database");
        }

        return userId;
    }


}
