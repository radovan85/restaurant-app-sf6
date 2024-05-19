package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.AddressService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderAddressService;
import com.radovan.spring.service.OrderItemService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.ProductService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private OrderAddressService orderAddressService;

	@Autowired
	private CartItemService cartItemService;

	@GetMapping(value = "/")
	public String adminHome() {
		return "fragments/admin :: fragmentContent";
	}

	@GetMapping(value = "/createProduct")
	public String renderProductForm(ModelMap map) {
		ProductDto product = new ProductDto();
		map.put("product", product);
		map.put("allCategories", product.getCategoryList());
		return "fragments/productForm :: fragmentContent";
	}

	@PostMapping(value = "/createProduct")
	public String createProduct(@ModelAttribute("product") ProductDto product, ModelMap map) {

		productService.addProduct(product);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/allProducts")
	public String allProductsList(ModelMap map) {
		List<ProductDto> allProducts = productService.listAll();
		map.put("allProducts", allProducts);
		map.put("recordsPerPage", 5);
		return "fragments/adminProductList :: fragmentContent";
	}

	@GetMapping(value = "/updateProduct/{productId}")
	public String renderUpdateForm(@PathVariable("productId") Integer productId, ModelMap map) {
		ProductDto product = new ProductDto();
		ProductDto currentProduct = productService.getProduct(productId);
		map.put("product", product);
		map.put("currentProduct", currentProduct);
		map.put("allCategories", product.getCategoryList());
		return "fragments/updateProduct :: fragmentContent";
	}

	@GetMapping(value = "/deleteProduct/{productId}")
	public String deleteProduct(@PathVariable("productId") Integer productId) {

		cartItemService.eraseAllByProductId(productId);
		productService.deleteProduct(productId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/productDetails/{productId}")
	public String getProductDetails(@PathVariable("productId") Integer productId, ModelMap map) {
		ProductDto currentProduct = productService.getProduct(productId);
		map.put("currentProduct", currentProduct);
		return "fragments/productDetails :: fragmentContent";
	}

	@GetMapping(value = "/allCustomers")
	public String listAllCustomers(ModelMap map) {
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 7);
		return "fragments/customerList :: fragmentContent";
	}

	@GetMapping(value = "/customerDetails/{customerId}")
	public String getCustomerDetails(@PathVariable("customerId") Integer customerId, ModelMap map) {
		CustomerDto customer = customerService.getCustomer(customerId);
		AddressDto address = addressService.getAddressById(customer.getAddressId());
		UserDto user = userService.getUserById(customer.getUserId());
		List<OrderDto> allOrders = orderService.listAllByCustomerId(customerId);
		map.put("customer", customer);
		map.put("address", address);
		map.put("user", user);
		map.put("allOrders", allOrders);
		return "fragments/customerDetails :: fragmentContent";
	}

	@GetMapping(value = "/allOrders")
	public String getAllOrders(ModelMap map) {
		List<OrderDto> allOrders = orderService.listAll();
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allOrders", allOrders);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 10);
		return "fragments/orderList :: fragmentContent";
	}

	@GetMapping(value = "/allOrdersToday")
	public String getAllOrdersToday(ModelMap map) {
		List<OrderDto> allOrders = orderService.getTodaysOrders();
		List<CustomerDto> allCustomers = customerService.listAll();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allOrders", allOrders);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 10);
		return "fragments/ordersTodayList :: fragmentContent";
	}

	@GetMapping(value = "/getOrder/{orderId}")
	public String orderDetails(@PathVariable("orderId") Integer orderId, ModelMap map) {

		OrderDto order = orderService.getOrder(orderId);
		CustomerDto customer = customerService.getCustomerByCartId(order.getCartId());
		OrderAddressDto address = orderAddressService.getAddressById(order.getAddressId());
		Float orderPrice = orderService.calculateOrderPrice(orderId);
		List<OrderItemDto> orderedItems = orderItemService.listAllByOrderId(orderId);
		map.put("order", order);
		map.put("address", address);
		map.put("orderPrice", orderPrice);
		map.put("orderedItems", orderedItems);
		map.put("customer", customer);
		return "fragments/orderDetails :: fragmentContent";
	}

	@GetMapping(value = "/deleteOrder/{orderId}")
	public String deleteOrder(@PathVariable("orderId") Integer orderId) {

		orderService.deleteOrder(orderId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/invalidPath")
	public String invalidImagePath() {
		return "fragments/invalidImagePath :: fragmentContent";
	}

	@GetMapping(value = "/deleteCustomer/{customerId}")
	public String removeCustomer(@PathVariable("customerId") Integer customerId) {
		customerService.deleteCustomer(customerId);
		return "fragments/homePage :: fragmentContent";
	}
}
