package com.lcwd.electronic.store.services;

import java.util.List;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.helper.PageableResponse;

public interface OrderService {
	
//	Create Order
	OrderDto createOrder(CreateOrderRequest createOrderRequest);
//	Remove Order
	void removeOrder(String orderId);
//	Get Orders of User
	List<OrderDto> getOrdersOfUser(String userId);
//	Get Orders ---- for the admin,if admin wants to see all orders of whole users...
	PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
}
