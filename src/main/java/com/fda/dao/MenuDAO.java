package com.fda.dao;

import com.fda.model.Menu;

import java.util.List;

public interface MenuDAO {
    void addMenu(Menu menu);
    Menu getMenuById(int menuId);
    List<Menu> getAllMenus();
    List<Menu> getMenusByRestaurantId(int restaurantId);
    void updateMenu(Menu menu);
    void deleteMenu(int menuId);
}
