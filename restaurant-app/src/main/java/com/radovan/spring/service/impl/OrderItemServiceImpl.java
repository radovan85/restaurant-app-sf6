package com.radovan.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemRepository itemRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	@Transactional(readOnly = true)
	public OrderItemDto getItem(Integer itemId) {
		// TODO Auto-generated method stub

		OrderItemEntity itemEntity = itemRepository.findById(itemId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The item has not been found")));
		return tempConverter.orderItemEntityToDto(itemEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderItemDto> listAllByOrderId(Integer orderId) {
		// TODO Auto-generated method stub
		List<OrderItemEntity> allItems = itemRepository.findAllByOrderId(orderId);
		return allItems.stream().map(tempConverter::orderItemEntityToDto).collect(Collectors.toList());
	}

}
