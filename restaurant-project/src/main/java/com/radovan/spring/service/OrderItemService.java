package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.OrderItemDto;

public interface OrderItemService {

	OrderItemDto getItem(Integer itemId);

	List<OrderItemDto> listAllByOrderId(Integer orderId);

	void eraseAllByOrderId(Integer orderId);
}
