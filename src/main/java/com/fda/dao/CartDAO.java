package com.fda.dao;

import com.fda.model.Cart;

import java.util.List;

public interface CartDAO {

    void addToCart(int userID,int menuID);

    void removeFromCart(int cartId);

    List<Cart> getCartByUserId(int userId);

    List<Cart> getAllCarts();

	boolean isMenuInCart(int menuID);

	void updateCartQuantityByMenuId(int menuId);
}
