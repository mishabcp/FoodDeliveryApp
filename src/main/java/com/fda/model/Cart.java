package com.fda.model;

public class Cart {

    private int cartId;
    private int userId;
    private int menuId;
    private int quantity;

    public Cart() {
        // Default constructor
    }

    public Cart(int cartId, int userId, int menuId, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.menuId = menuId;
        this.quantity = quantity;
    }
    public Cart( int userId, int menuId, int quantity) {

    	this.userId = userId;
    	this.menuId = menuId;
    	this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

  

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", menuId=" + menuId +
                ", quantity=" + quantity +
                '}';
    }
}
