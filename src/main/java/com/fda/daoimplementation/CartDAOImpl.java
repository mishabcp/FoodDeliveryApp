package com.fda.daoimplementation;

import com.fda.dao.CartDAO;

import com.fda.model.Cart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {

    private static Connection connection = null;

    private static final String INSERT_QUERY = "INSERT INTO `cart` (`UserID`, `MenuID`, `Quantity`) VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM `cart` WHERE `CartID` = ?";
    private static final String SELECT_BY_USER_QUERY = "SELECT * FROM `cart` WHERE `UserID` = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM `cart`";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fdadb", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing database connection");
        }
    }

    public void addToCart(int userID,int menuID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setInt(1, userID );
            preparedStatement.setInt(2, menuID);
            preparedStatement.setInt(3, 1);

            preparedStatement.executeUpdate();
            System.out.println("updated");

        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Error adding to cart in the database");
        }
    }

    @Override
    public void removeFromCart(int cartId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, cartId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error removing from cart in the database");
        }
    }
    
    public void removeEntryFromCart(int menuId , int userId) {
    	try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `cart` WHERE `MenuID` = ? and `UserID` = ?")) {
    		preparedStatement.setInt(1, menuId);
    		preparedStatement.setInt(2, userId);
    		preparedStatement.executeUpdate();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		throw new RuntimeException("Error removing from cart in the database");
    	}
    }

    public void updateCart(int menuId, int userId , int quantity) {
    	try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `cart` SET `Quantity` = ? WHERE `MenuID` = ? and `UserID` = ?")) {
    		preparedStatement.setInt(1, quantity);
    		preparedStatement.setInt(2, menuId);
    		preparedStatement.setInt(3, userId);
    		preparedStatement.executeUpdate();
    		System.out.println("updated");
    	} catch (SQLException e) {
    		e.printStackTrace();
    		throw new RuntimeException("Error updating cart quantity in the database");
    	}
    }
    @Override
    public void updateCartQuantityByMenuId(int menuId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `cart` SET `Quantity` = `Quantity` + 1 WHERE `MenuID` = ?")) {
            preparedStatement.setInt(1, menuId);
            preparedStatement.executeUpdate();
            System.out.println("updated");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating cart quantity in the database");
        }
    }

    @Override
    public boolean isMenuInCart(int menuID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM `cart` WHERE `MenuID` = ?")) {
            preparedStatement.setInt(1, menuID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking if menu is in the cart in the database");
        }
        return false;
    }

    @Override
    public List<Cart> getCartByUserId(int userId) {
        List<Cart> cartList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_USER_QUERY)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    cartList.add(extractCartFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving cart by user ID from the database");
        }
        return cartList;
    }

    @Override
    public List<Cart> getAllCarts() {
        List<Cart> cartList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                cartList.add(extractCartFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all carts from the database");
        }
        return cartList;
    }

    private Cart extractCartFromResultSet(ResultSet resultSet) throws SQLException {
        int cartId = resultSet.getInt("CartID");
        int userId = resultSet.getInt("UserID");
        int menuId = resultSet.getInt("MenuID");
        int quantity = resultSet.getInt("Quantity");

        return new Cart(cartId, userId, menuId, quantity);
    }
    public void addItemToCart(int userId, int menuId) {
    	if (isMenuInCart(menuId)) {
    		updateCartQuantityByMenuId(menuId);
    	}
    	else {
    		addToCart(userId,menuId);
    	}
    }
    
    public void deleteCartEntries(int userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `cart` WHERE `UserID` = ?")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
            System.out.println("Cart entries deleted for user with ID: " + userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting cart entries in the database");
        }
    }
    
    
    public List<Cart> getCartEntriesByUserId(int userId) {
        List<Cart> cartEntries = new ArrayList<>();
        String query = "SELECT `UserID`, `MenuID`, `Quantity` FROM `cart` WHERE `UserID` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int menuId = resultSet.getInt("MenuID");
                    int quantity = resultSet.getInt("Quantity");
                    cartEntries.add(new Cart(userId, menuId, quantity));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving cart entries by user ID from the database");
        }

        return cartEntries;
    }
    
    public void incrementQuantityByMenuId(int menuId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `cart` SET `Quantity` = `Quantity` + 1 WHERE `MenuID` = ?")) {
            preparedStatement.setInt(1, menuId);
            preparedStatement.executeUpdate();
            System.out.println("Quantity incremented for Menu ID: " + menuId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error incrementing quantity in the database");
        }
    }

    public void decrementQuantityByMenuId(int menuId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `cart` SET `Quantity` = `Quantity` - 1 WHERE `MenuID` = ? AND `Quantity` > 1")) {
            preparedStatement.setInt(1, menuId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("Cannot decrement quantity below 1");
            }
            System.out.println("Quantity decremented for Menu ID: " + menuId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error decrementing quantity in the database");
        }
    }




}
