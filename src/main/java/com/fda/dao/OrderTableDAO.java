package com.fda.dao;

import java.util.List;
import com.fda.model.OrderTable;

public interface OrderTableDAO {
    
    void insertOrder(OrderTable order);

   
    List<OrderTable> getOrdersByUserId(int userId);

    List<OrderTable> getOrdersByMenuId(int menuId);

    List<OrderTable> getAllOrders();
}
