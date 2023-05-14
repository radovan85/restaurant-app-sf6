package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.OrderDto;



public interface OrderService {

	OrderDto addOrder();
	
	List<OrderDto> getTodaysOrders();
	
	List<OrderDto> listAllByCustomerId(Integer customerId);
	
	Float calculateOrderPrice(Integer orderId);
	
	List<OrderDto> listAll();
	
	OrderDto getOrder(Integer orderId);
	
	void deleteOrder(Integer orderId);
}
