package com.fda.dao;

import com.fda.model.Restaurant;

import java.util.List;

public interface RestaurantDAO {
    void addRestaurant(Restaurant restaurant);
    Restaurant getRestaurantById(int restaurantId);
    List<Restaurant> getAllRestaurants();
    void updateRestaurant(Restaurant restaurant);
    void deleteRestaurant(int restaurantId);
}
