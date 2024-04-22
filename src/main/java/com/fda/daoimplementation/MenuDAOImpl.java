package com.fda.daoimplementation;

import com.fda.dao.MenuDAO;
import com.fda.model.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAOImpl implements MenuDAO {

    private static Connection connection = null;

    private static final String INSERT_QUERY = "INSERT INTO `Menu` (`RestaurantID`, `ItemName`, `Description`, `Price`, `IsAvailable` , `ImagePath`) VALUES (?, ?, ?, ?, ? , ?)";
    private static final String DELETE_QUERY = "DELETE FROM `Menu` WHERE `MenuID` = ?";
    private static final String UPDATE_QUERY = "UPDATE `Menu` SET `RestaurantID` = ?, `ItemName` = ?, `Description` = ?, `Price` = ?, `IsAvailable` = ? , `ImagePath` = ? WHERE `MenuID` = ?";
    private static final String SELECT_QUERY = "SELECT * FROM `Menu` WHERE `MenuID` = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM `Menu`";
    private static final String SELECT_BY_RESTAURANT_QUERY = "SELECT * FROM `Menu` WHERE `RestaurantID` = ?";
    private static final String SELECT_RESTAURANT_ID_BY_MENU_ID_QUERY = "SELECT `RestaurantID` FROM `Menu` WHERE `MenuID` = ?";

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
    public void addMenu(Menu menu) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, menu.getRestaurantID());
            preparedStatement.setString(2, menu.getItemName());
            preparedStatement.setString(3, menu.getDescription());
            preparedStatement.setDouble(4, menu.getPrice());
            preparedStatement.setBoolean(5, menu.isAvailable());
            preparedStatement.setString(6, menu.getImagePath());

            preparedStatement.executeUpdate();

            // Retrieve the generated keys (if any)
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    menu.setMenuID(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Error adding menu to the database");
        }
    }

    @Override
    public void deleteMenu(int menuId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, menuId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting menu from the database");
        }
    }

    @Override
    public void updateMenu(Menu menu) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setInt(1, menu.getRestaurantID());
            preparedStatement.setString(2, menu.getItemName());
            preparedStatement.setString(3, menu.getDescription());
            preparedStatement.setDouble(4, menu.getPrice());
            preparedStatement.setBoolean(5, menu.isAvailable());
            preparedStatement.setString(6, menu.getImagePath());
            preparedStatement.setInt(7, menu.getMenuID());

            preparedStatement.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
            throw new RuntimeException("Error updating menu in the database");
        }
    }

    @Override
    public Menu getMenuById(int menuId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setInt(1, menuId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractMenuFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving menu from the database");
        }
        return null;
    }

    @Override
    public List<Menu> getAllMenus() {
        List<Menu> menuList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY)) {

            while (resultSet.next()) {
                menuList.add(extractMenuFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all menus from the database");
        }
        return menuList;
    }

    @Override
    public List<Menu> getMenusByRestaurantId(int restaurantId) {
        List<Menu> menuList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_RESTAURANT_QUERY)) {
            preparedStatement.setInt(1, restaurantId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    menuList.add(extractMenuFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving menus by restaurant ID from the database");
        }
        return menuList;
    }

    private Menu extractMenuFromResultSet(ResultSet resultSet) throws SQLException {
        int menuId = resultSet.getInt("MenuID");
        int restaurantId = resultSet.getInt("RestaurantID");
        String itemName = resultSet.getString("ItemName");
        String description = resultSet.getString("Description");
        double price = resultSet.getDouble("Price");
        boolean isAvailable = resultSet.getBoolean("IsAvailable");
        String imagePath = resultSet.getString("ImagePath");

        return new Menu(menuId, restaurantId, itemName, description, price, isAvailable, imagePath);
    }
    
    public int getRestaurantIdByMenuId(int menuId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RESTAURANT_ID_BY_MENU_ID_QUERY)) {
            preparedStatement.setInt(1, menuId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("RestaurantID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving restaurant ID by menu ID from the database");
        }
        return -1; // or throw an exception if you prefer
    }
}
