package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.service.AddressService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Autowired
	private AddressService addressService;

	@Override
	@Transactional
	public OrderDto addOrder() {
		// TODO Auto-generated method stub
		OrderDto returnValue = new OrderDto();
		CustomerDto customer = customerService.getCurrentCustomer();
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		cartService.validateCart(cart.getCartId());
		Float grandTotal = cartService.calculateCartPrice(cart.getCartId());
		returnValue.setCartId(cart.getCartId());
		returnValue.setOrderPrice(grandTotal);
		AddressDto address = addressService.getAddressById(customer.getAddressId());
		OrderAddressDto orderAddress = tempConverter.addressToOrderAddress(address);
		OrderAddressEntity orderAddressEntity = tempConverter.orderAddressDtoToEntity(orderAddress);
		OrderAddressEntity storedAddress = orderAddressRepository.save(orderAddressEntity);
		OrderEntity orderEntity = tempConverter.orderDtoToEntity(returnValue);
		orderEntity.setAddress(storedAddress);
		OrderEntity storedOrder = orderRepository.save(orderEntity);

		List<OrderItemDto> orderedItems = new ArrayList<>();

		List<CartItemDto> cartItems = cartItemService.listAllByCartId(cart.getCartId());

		cartItems.forEach((cartItemDto) -> {
			OrderItemDto orderItem = tempConverter.cartItemToOrderItem(cartItemDto);
			orderedItems.add(orderItem);
		});

		List<OrderItemEntity> allOrderedItems = new ArrayList<>();

		for (OrderItemDto orderItem : orderedItems) {
			orderItem.setOrderId(storedOrder.getOrderId());
			OrderItemEntity orderItemEntity = tempConverter.orderItemDtoToEntity(orderItem);
			OrderItemEntity storedItem = orderItemRepository.save(orderItemEntity);
			allOrderedItems.add(storedItem);
		}

		storedOrder.getOrderedItems().clear();
		storedOrder.getOrderedItems().addAll(allOrderedItems);
		storedOrder = orderRepository.saveAndFlush(storedOrder);
		returnValue = tempConverter.orderEntityToDto(storedOrder);
		cartItemService.eraseAllCartItems(cart.getCartId());
		cartService.refreshCartState(cart.getCartId());

		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDto> getTodaysOrders() {
		// TODO Auto-generated method stub
		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDateStr = currentDate.format(formatter);
		String timestamp1Str = currentDateStr + " 00:00:00";
		String timestamp2Str = currentDateStr + " 23:59:59";
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		Timestamp timestamp1 = Timestamp.valueOf(LocalDateTime.parse(timestamp1Str, formatter));
		Timestamp timestamp2 = Timestamp.valueOf(LocalDateTime.parse(timestamp2Str, formatter));

		List<OrderEntity> allOrders = orderRepository.findAllTodaysOrders(timestamp1, timestamp2);
		return allOrders.stream().map(tempConverter::orderEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDto> listAllByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		CustomerDto customer = customerService.getCustomer(customerId);
		List<OrderEntity> allOrders = orderRepository.findAllByCartId(customer.getCartId());

		return allOrders.stream().map(tempConverter::orderEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Float calculateOrderPrice(Integer orderId) {

		return orderItemRepository.calculateGrandTotal(orderId).orElse(0f);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDto> listAll() {
		// TODO Auto-generated method stub
		List<OrderEntity> allOrders = orderRepository.findAll();
		return allOrders.stream().map(tempConverter::orderEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public OrderDto getOrder(Integer orderId) {
		// TODO Auto-generated method stub

		OrderEntity orderEntity = orderRepository.findById(orderId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The order has not been found!")));
		return tempConverter.orderEntityToDto(orderEntity);

	}

	@Override
	@Transactional
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		orderRepository.deleteById(orderId);
		orderRepository.flush();
	}

}
