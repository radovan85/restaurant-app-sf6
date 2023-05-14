package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.AddressEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.ProductEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.AddressRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.ProductRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;

@Component
public class TempConverter {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public CartDto cartEntityToDto(CartEntity cartEntity) {
		CartDto returnValue = mapper.map(cartEntity, CartDto.class);

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(cartEntity.getCustomer());
		if (customerOpt.isPresent()) {
			CustomerEntity customerEntity = customerOpt.get();
			returnValue.setCustomerId(customerEntity.getCustomerId());
		}

		Optional<List<CartItemEntity>> cartItemsOpt = Optional.ofNullable(cartEntity.getCartItems());
		List<Integer> cartItemsIds = new ArrayList<Integer>();
		if (!cartItemsOpt.isEmpty()) {
			List<CartItemEntity> cartItems = cartItemsOpt.get();
			cartItems.forEach((item) -> {
				cartItemsIds.add(item.getCartItemId());
			});
		}

		returnValue.setCartItemsIds(cartItemsIds);

		return returnValue;

	}

	public CartEntity cartDtoToEntity(CartDto cartDto) {
		CartEntity returnValue = mapper.map(cartDto, CartEntity.class);

		Optional<Integer> customerIdOpt = Optional.ofNullable(cartDto.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customer = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customer);
		}

		Optional<List<Integer>> cartItemsIdsOpt = Optional.ofNullable(cartDto.getCartItemsIds());
		List<CartItemEntity> cartItems = new ArrayList<>();
		if (!cartItemsIdsOpt.isEmpty()) {
			List<Integer> cartItemsIds = cartItemsIdsOpt.get();
			cartItemsIds.forEach((itemId) -> {
				CartItemEntity itemEntity = cartItemRepository.findById(itemId).get();
				cartItems.add(itemEntity);
			});
		}

		returnValue.setCartItems(cartItems);
		return returnValue;
	}

	public CartItemDto cartItemEntityToDto(CartItemEntity itemEntity) {
		CartItemDto returnValue = mapper.map(itemEntity, CartItemDto.class);

		Optional<CartEntity> cartOpt = Optional.ofNullable(itemEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getCartId());
		}

		Optional<ProductEntity> productOpt = Optional.ofNullable(itemEntity.getProduct());
		if (productOpt.isPresent()) {
			returnValue.setProductId(productOpt.get().getProductId());
		}

		return returnValue;
	}

	public CartItemEntity cartItemDtoToEntity(CartItemDto itemDto) {
		CartItemEntity returnValue = mapper.map(itemDto, CartItemEntity.class);

		Optional<Integer> cartIdOpt = Optional.ofNullable(itemDto.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> productIdOpt = Optional.ofNullable(itemDto.getProductId());
		if (productIdOpt.isPresent()) {
			Integer productId = productIdOpt.get();
			ProductEntity productEntity = productRepository.findById(productId).get();
			returnValue.setProduct(productEntity);
		}

		return returnValue;
	}

	public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
		CustomerDto returnValue = mapper.map(customerEntity, CustomerDto.class);

		Optional<UserEntity> userOpt = Optional.ofNullable(customerEntity.getUser());
		if (userOpt.isPresent()) {
			returnValue.setUserId(userOpt.get().getId());
		}

		Optional<List<OrderEntity>> ordersOpt = Optional.ofNullable(customerEntity.getOrders());
		List<Integer> ordersIds = new ArrayList<>();
		if (!ordersOpt.isEmpty()) {
			List<OrderEntity> orders = ordersOpt.get();
			orders.forEach((orderEntity) -> {
				ordersIds.add(orderEntity.getOrderId());
			});
		}

		returnValue.setOrdersIds(ordersIds);

		Optional<CartEntity> cartOpt = Optional.ofNullable(customerEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getCartId());
		}

		Optional<AddressEntity> addressOpt = Optional.ofNullable(customerEntity.getAddress());
		if (addressOpt.isPresent()) {
			returnValue.setAddressId(addressOpt.get().getAddressId());
		}

		return returnValue;
	}

	public CustomerEntity customerDtoToEntity(CustomerDto customer) {
		CustomerEntity returnValue = mapper.map(customer, CustomerEntity.class);

		Optional<Integer> userIdOpt = Optional.ofNullable(customer.getUserId());
		if (userIdOpt.isPresent()) {
			Integer userId = userIdOpt.get();
			UserEntity userEntity = userRepository.findById(userId).get();
			returnValue.setUser(userEntity);
		}

		Optional<List<Integer>> ordersIdsOpt = Optional.ofNullable(customer.getOrdersIds());
		List<OrderEntity> orders = new ArrayList<>();
		if (!ordersIdsOpt.isEmpty()) {
			List<Integer> ordersIds = ordersIdsOpt.get();
			ordersIds.forEach((orderId) -> {
				OrderEntity orderEntity = orderRepository.findById(orderId).get();
				orders.add(orderEntity);
			});
		}

		returnValue.setOrders(orders);

		Optional<Integer> cartIdOpt = Optional.ofNullable(customer.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> addressIdOpt = Optional.ofNullable(customer.getAddressId());
		if (addressIdOpt.isPresent()) {
			Integer addressId = addressIdOpt.get();
			AddressEntity address = addressRepository.findById(addressId).get();
			returnValue.setAddress(address);
		}

		return returnValue;
	}

	public OrderDto orderEntityToDto(OrderEntity orderEntity) {
		OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(orderEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		Optional<OrderAddressEntity> addressOpt = Optional.ofNullable(orderEntity.getAddress());
		if (addressOpt.isPresent()) {
			returnValue.setAddressId(addressOpt.get().getOrderAddressId());
		}

		Optional<List<OrderItemEntity>> orderItemsOpt = Optional.ofNullable(orderEntity.getOrderItems());
		List<Integer> orderItemsIds = new ArrayList<>();
		if (!orderItemsOpt.isEmpty()) {
			List<OrderItemEntity> orderItems = orderItemsOpt.get();
			orderItems.forEach((itemEntity) -> {
				orderItemsIds.add(itemEntity.getOrderItemId());
			});
		}

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(orderEntity.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			String createdAtStr = createdAtOpt.get().toLocalDateTime().format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		returnValue.setOrderItemsIds(orderItemsIds);
		return returnValue;
	}

	public OrderEntity orderDtoToEntity(OrderDto order) {
		OrderEntity returnValue = mapper.map(order, OrderEntity.class);

		Optional<Integer> customerIdOpt = Optional.ofNullable(order.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		Optional<Integer> addressIdOpt = Optional.ofNullable(order.getAddressId());
		if (addressIdOpt.isPresent()) {
			Integer addressId = addressIdOpt.get();
			OrderAddressEntity addressEntity = orderAddressRepository.findById(addressId).get();
			returnValue.setAddress(addressEntity);
		}

		Optional<List<Integer>> orderItemsIdsOpt = Optional.ofNullable(order.getOrderItemsIds());
		List<OrderItemEntity> orderItems = new ArrayList<>();
		if (!orderItemsIdsOpt.isEmpty()) {
			List<Integer> orderItemsIds = orderItemsIdsOpt.get();
			orderItemsIds.forEach((itemId) -> {
				OrderItemEntity itemEntity = orderItemRepository.findById(itemId).get();
				orderItems.add(itemEntity);
			});
		}

		returnValue.setOrderItems(orderItems);
		return returnValue;
	}

	public OrderItemDto orderItemEntityToDto(OrderItemEntity itemEntity) {
		OrderItemDto returnValue = mapper.map(itemEntity, OrderItemDto.class);

		Optional<OrderEntity> orderOpt = Optional.ofNullable(itemEntity.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderItemEntity orderItemDtoToEntity(OrderItemDto itemDto) {
		OrderItemEntity returnValue = mapper.map(itemDto, OrderItemEntity.class);

		Optional<Integer> orderIdOpt = Optional.ofNullable(itemDto.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public ProductDto productEntityToDto(ProductEntity productEntity) {
		ProductDto returnValue = mapper.map(productEntity, ProductDto.class);
		Float price = Float.valueOf(decfor.format(returnValue.getProductPrice()));
		returnValue.setProductPrice(price);
		return returnValue;
	}

	public ProductEntity productDtoToEntity(ProductDto product) {
		ProductEntity returnValue = mapper.map(product, ProductEntity.class);
		Float price = Float.valueOf(decfor.format(returnValue.getProductPrice()));
		returnValue.setProductPrice(price);
		return returnValue;
	}

	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		returnValue.setEnabled(userEntity.getEnabled());
		Optional<List<RoleEntity>> rolesOpt = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!rolesOpt.isEmpty()) {
			rolesOpt.get().forEach((roleEntity) -> {
				rolesIds.add(roleEntity.getId());
			});
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIdsOpt = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIdsOpt.isEmpty()) {
			rolesIdsOpt.get().forEach((roleId) -> {
				RoleEntity role = roleRepository.findById(roleId).get();
				roles.add(role);
			});
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		List<UserEntity> users = roleEntity.getUsers();
		List<Integer> userIds = new ArrayList<>();

		users.forEach((user) -> {
			userIds.add(user.getId());
		});

		returnValue.setUsersIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		List<Integer> usersIds = roleDto.getUsersIds();
		List<UserEntity> users = new ArrayList<>();

		usersIds.forEach((userId) -> {
			UserEntity userEntity = userRepository.findById(userId).get();
			users.add(userEntity);
		});

		returnValue.setUsers(users);
		return returnValue;
	}

	public OrderItemDto cartItemDtoToOrderItemDto(CartItemDto cartItem) {
		OrderItemDto returnValue = mapper.map(cartItem, OrderItemDto.class);
		return returnValue;
	}

	public OrderItemEntity cartItemEntityToOrderItemEntity(CartItemEntity cartItem) {
		OrderItemEntity returnValue = mapper.map(cartItem, OrderItemEntity.class);
		return returnValue;
	}

	public OrderItemEntity cartItemToOrderItemEntity(CartItemEntity cartItemEntity) {
		OrderItemEntity returnValue = mapper.map(cartItemEntity, OrderItemEntity.class);

		Optional<ProductEntity> productOpt = Optional.ofNullable(cartItemEntity.getProduct());
		if (productOpt.isPresent()) {
			ProductEntity product = productOpt.get();
			returnValue.setProductName(product.getProductName());
			returnValue.setProductPrice(product.getProductPrice());
		}

		return returnValue;
	}

	public AddressDto addressEntityToDto(AddressEntity addressEntity) {
		AddressDto returnValue = mapper.map(addressEntity, AddressDto.class);

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(addressEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		return returnValue;
	}

	public AddressEntity addressDtoToEntity(AddressDto address) {
		AddressEntity returnValue = mapper.map(address, AddressEntity.class);

		Optional<Integer> customerIdOpt = Optional.ofNullable(address.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public OrderAddressDto orderAddressEntityToDto(OrderAddressEntity address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		Optional<OrderEntity> orderOpt = Optional.ofNullable(address.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderAddressEntity orderAddressDtoToEntity(OrderAddressDto address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		Optional<Integer> orderIdOpt = Optional.ofNullable(address.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public OrderAddressEntity addressToOrderAddress(AddressEntity address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		return returnValue;
	}
}
