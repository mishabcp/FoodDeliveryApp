package com.fda.model;

import java.sql.Timestamp;

public class OrderTable {
    private int orderId;
    private int userId;
    private int menuId;
    private int quantity;
    private Timestamp orderDateAndTime;

    public OrderTable() {
        // Default constructor
    }

    public OrderTable(int userId, int menuId, int quantity) {
        this.userId = userId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderTable(int orderId, int userId, int menuId, int quantity, Timestamp orderDateAndTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.menuId = menuId;
        this.quantity = quantity;
        this.orderDateAndTime = orderDateAndTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMenuId() { 
        return this.menuId;
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

    public Timestamp getOrderDateAndTime() {
        return orderDateAndTime;
    }

    public void setOrderDateAndTime(Timestamp orderDateAndTime) {
        this.orderDateAndTime = orderDateAndTime;
    }
}
