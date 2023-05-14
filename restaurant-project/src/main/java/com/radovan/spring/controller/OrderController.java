package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.service.AddressService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.ProductService;

@Controller
@RequestMapping(value = "/orders")
public class OrderController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	@GetMapping(value = "/confirmOrder/{cartId}")
	public String confirmOrder(@PathVariable("cartId") Integer cartId, ModelMap map) {
		OrderDto order = new OrderDto();
		cartService.validateCart(cartId);
		CustomerDto customer = customerService.getCustomerByCartId(cartId);
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(cartId);
		AddressDto address = addressService.getAddressById(customer.getAddressId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		List<ProductDto> allProducts = productService.listAll();

		map.put("order", order);
		map.put("customer", customer);
		map.put("allCartItems", allCartItems);
		map.put("address", address);
		map.put("cart", cart);
		map.put("allProducts", allProducts);
		return "fragments/orderConfirmation :: ajaxLoadedContent";
	}

	@PostMapping(value = "/processOrder")
	public String processOrder() {
		orderService.addOrder();
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/orderCompleted")
	public String orderCompletedReport() {
		return "fragments/orderCompleted :: ajaxLoadedContent";
	}
}
