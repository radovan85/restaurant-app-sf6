package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private ZoneId targetZone = ZoneId.of("Europe/Belgrade");

	public CartDto cartEntityToDto(CartEntity cartEntity) {
		CartDto returnValue = mapper.map(cartEntity, CartDto.class);
		Optional<Float> cartPriceOptional = Optional.ofNullable(cartEntity.getCartPrice());
		if (!cartPriceOptional.isPresent()) {
			returnValue.setCartPrice(0f);
		}
		Optional<CustomerEntity> customerOptional = Optional.ofNullable(cartEntity.getCustomer());
		if (customerOptional.isPresent()) {
			returnValue.setCustomerId(customerOptional.get().getCustomerId());
		}

		List<Integer> itemsIds = new ArrayList<>();
		Optional<List<CartItemEntity>> cartItemsOptional = Optional.ofNullable(cartEntity.getCartItems());
		if (!cartItemsOptional.isEmpty()) {
			cartItemsOptional.get().forEach((itemEntity) -> {
				Integer itemId = itemEntity.getCartItemId();
				itemsIds.add(itemId);
			});
		}
		returnValue.setCartItemsIds(itemsIds);

		Float cartPrice = Float.valueOf(decfor.format(returnValue.getCartPrice()));
		returnValue.setCartPrice(cartPrice);
		return returnValue;

	}

	public CartEntity cartDtoToEntity(CartDto cartDto) {
		CartEntity returnValue = mapper.map(cartDto, CartEntity.class);
		Optional<Float> cartPriceOptional = Optional.ofNullable(cartDto.getCartPrice());
		if (!cartPriceOptional.isPresent()) {
			returnValue.setCartPrice(0f);
		}
		Optional<Integer> customerIdOptional = Optional.ofNullable(cartDto.getCustomerId());
		if (customerIdOptional.isPresent()) {
			Integer customerId = customerIdOptional.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		List<CartItemEntity> cartItems = new ArrayList<>();
		Optional<List<Integer>> itemIdsOptional = Optional.ofNullable(cartDto.getCartItemsIds());

		if (!itemIdsOptional.isEmpty()) {
			itemIdsOptional.get().forEach((itemId) -> {
				CartItemEntity itemEntity = cartItemRepository.findById(itemId).get();
				cartItems.add(itemEntity);
			});
		}
		returnValue.setCartItems(cartItems);
		Float cartPrice = Float.valueOf(decfor.format(returnValue.getCartPrice()));
		returnValue.setCartPrice(cartPrice);
		return returnValue;
	}

	public CartItemDto cartItemEntityToDto(CartItemEntity itemEntity) {
		CartItemDto returnValue = mapper.map(itemEntity, CartItemDto.class);

		Optional<CartEntity> cartOptional = Optional.ofNullable(itemEntity.getCart());
		if (cartOptional.isPresent()) {
			returnValue.setCartId(cartOptional.get().getCartId());
		}

		Optional<ProductEntity> productOptional = Optional.ofNullable(itemEntity.getProduct());
		if (productOptional.isPresent()) {
			returnValue.setProductId(productOptional.get().getProductId());
		}

		return returnValue;
	}

	public CartItemEntity cartItemDtoToEntity(CartItemDto itemDto) {
		CartItemEntity returnValue = mapper.map(itemDto, CartItemEntity.class);

		Optional<Integer> cartIdOptional = Optional.ofNullable(itemDto.getCartId());
		if (cartIdOptional.isPresent()) {
			Integer cartId = cartIdOptional.get();
			CartEntity cartEntity = cartRepository.findById(cartId).orElse(null);
			if (cartEntity != null) {
				returnValue.setCart(cartEntity);
			}

		}

		Optional<Integer> productIdOptional = Optional.ofNullable(itemDto.getProductId());
		if (productIdOptional.isPresent()) {
			Integer productId = productIdOptional.get();
			ProductEntity productEntity = productRepository.findById(productId).orElse(null);
			if (productEntity != null) {
				returnValue.setProduct(productEntity);
			}

		}

		return returnValue;
	}

	public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
		CustomerDto returnValue = mapper.map(customerEntity, CustomerDto.class);

		Optional<UserEntity> userOptional = Optional.ofNullable(customerEntity.getUser());
		if (userOptional.isPresent()) {
			returnValue.setUserId(userOptional.get().getId());
		}

		Optional<CartEntity> cartOptional = Optional.ofNullable(customerEntity.getCart());
		if (cartOptional.isPresent()) {
			returnValue.setCartId(cartOptional.get().getCartId());
		}

		Optional<AddressEntity> addressOptional = Optional.ofNullable(customerEntity.getAddress());
		if (addressOptional.isPresent()) {
			returnValue.setAddressId(addressOptional.get().getAddressId());
		}

		return returnValue;
	}

	public CustomerEntity customerDtoToEntity(CustomerDto customer) {
		CustomerEntity returnValue = mapper.map(customer, CustomerEntity.class);

		Optional<Integer> userIdOptional = Optional.ofNullable(customer.getUserId());
		if (userIdOptional.isPresent()) {
			Integer userId = userIdOptional.get();
			UserEntity userEntity = userRepository.findById(userId).orElse(null);
			if (userEntity != null) {
				returnValue.setUser(userEntity);
			}

		}

		Optional<Integer> cartIdOptional = Optional.ofNullable(customer.getCartId());
		if (cartIdOptional.isPresent()) {
			Integer cartId = cartIdOptional.get();
			CartEntity cartEntity = cartRepository.findById(cartId).orElse(null);
			if (cartEntity != null) {
				returnValue.setCart(cartEntity);
			}

		}

		Optional<Integer> addressIdOptional = Optional.ofNullable(customer.getAddressId());
		if (addressIdOptional.isPresent()) {
			Integer addressId = addressIdOptional.get();
			AddressEntity address = addressRepository.findById(addressId).orElse(null);
			if (address != null) {
				returnValue.setAddress(address);
			}

		}

		return returnValue;
	}

	public OrderDto orderEntityToDto(OrderEntity orderEntity) {
		OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);

		Optional<OrderAddressEntity> addressOptional = Optional.ofNullable(orderEntity.getAddress());
		if (addressOptional.isPresent()) {
			returnValue.setAddressId(addressOptional.get().getOrderAddressId());
		}

		Optional<List<OrderItemEntity>> orderItemsOptional = Optional.ofNullable(orderEntity.getOrderedItems());
		List<Integer> orderItemsIds = new ArrayList<>();
		if (!orderItemsOptional.isEmpty()) {
			orderItemsOptional.get().forEach((itemEntity) -> {
				orderItemsIds.add(itemEntity.getOrderItemId());
			});
		}

		returnValue.setOrderedItemsIds(orderItemsIds);

		Optional<Timestamp> createdAtOptional = Optional.ofNullable(orderEntity.getCreatedAt());
		if (createdAtOptional.isPresent()) {

			ZonedDateTime createdAt = createdAtOptional.get().toInstant().atZone(targetZone);
			String createdAtStr = createdAt.format(formatter);
			returnValue.setCreatedAt(createdAtStr);
		}

		Optional<CartEntity> cartOptional = Optional.ofNullable(orderEntity.getCart());
		if (cartOptional.isPresent()) {
			returnValue.setCartId(cartOptional.get().getCartId());
		}

		return returnValue;
	}

	public OrderEntity orderDtoToEntity(OrderDto order) {
		OrderEntity returnValue = mapper.map(order, OrderEntity.class);

		Optional<Integer> addressIdOptional = Optional.ofNullable(order.getAddressId());
		if (addressIdOptional.isPresent()) {
			Integer addressId = addressIdOptional.get();
			OrderAddressEntity addressEntity = orderAddressRepository.findById(addressId).orElse(null);
			if (addressEntity != null) {
				returnValue.setAddress(addressEntity);
			}
		}

		Optional<List<Integer>> orderItemsIdsOptional = Optional.ofNullable(order.getOrderedItemsIds());
		List<OrderItemEntity> orderItems = new ArrayList<>();
		if (!orderItemsIdsOptional.isEmpty()) {

			orderItemsIdsOptional.get().forEach((itemId) -> {
				OrderItemEntity itemEntity = orderItemRepository.findById(itemId).orElse(null);
				if (itemEntity != null) {
					orderItems.add(itemEntity);
				}
			});
		}

		Optional<String> createdAtOptional = Optional.ofNullable(order.getCreatedAt());
		if (createdAtOptional.isPresent()) {
			String createdAtStr = createdAtOptional.get();
			ZonedDateTime createdAt = ZonedDateTime.parse(createdAtStr, formatter.withZone(targetZone));
			returnValue.setCreatedAt(Timestamp.valueOf(createdAt.toLocalDateTime()));
		}

		Optional<Integer> cartIdOptional = Optional.ofNullable(order.getCartId());
		if (cartIdOptional.isPresent()) {
			Integer cartId = cartIdOptional.get();
			CartEntity cartEntity = cartRepository.findById(cartId).orElse(null);
			if (cartEntity != null) {
				returnValue.setCart(cartEntity);
			}
		}

		returnValue.setOrderedItems(orderItems);
		return returnValue;
	}

	public OrderItemDto orderItemEntityToDto(OrderItemEntity itemEntity) {
		OrderItemDto returnValue = mapper.map(itemEntity, OrderItemDto.class);

		Optional<OrderEntity> orderOptional = Optional.ofNullable(itemEntity.getOrder());
		if (orderOptional.isPresent()) {
			returnValue.setOrderId(orderOptional.get().getOrderId());
		}

		return returnValue;
	}

	public OrderItemEntity orderItemDtoToEntity(OrderItemDto itemDto) {
		OrderItemEntity returnValue = mapper.map(itemDto, OrderItemEntity.class);

		Optional<Integer> orderIdOptional = Optional.ofNullable(itemDto.getOrderId());
		if (orderIdOptional.isPresent()) {
			Integer orderId = orderIdOptional.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
			if (orderEntity != null) {
				returnValue.setOrder(orderEntity);
			}
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
		Optional<Byte> enabledOptional = Optional.ofNullable(userEntity.getEnabled());
		if (enabledOptional.isPresent()) {
			returnValue.setEnabled(Short.valueOf(enabledOptional.get()));
		}

		Optional<List<RoleEntity>> rolesOptional = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!rolesOptional.isEmpty()) {
			rolesOptional.get().forEach((roleEntity) -> {
				rolesIds.add(roleEntity.getId());
			});
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIdsOptional = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIdsOptional.isEmpty()) {
			rolesIdsOptional.get().forEach((roleId) -> {
				RoleEntity role = roleRepository.findById(roleId).get();
				roles.add(role);
			});
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		Optional<List<UserEntity>> usersOptional = Optional.ofNullable(roleEntity.getUsers());
		List<Integer> userIds = new ArrayList<>();

		if (!usersOptional.isEmpty()) {
			usersOptional.get().forEach((user) -> {
				userIds.add(user.getId());
			});
		}

		returnValue.setUsersIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		Optional<List<Integer>> usersIdsOptional = Optional.ofNullable(roleDto.getUsersIds());
		List<UserEntity> users = new ArrayList<>();

		if (!usersIdsOptional.isEmpty()) {
			usersIdsOptional.get().forEach((userId) -> {
				UserEntity userEntity = userRepository.findById(userId).get();
				users.add(userEntity);
			});
		}

		returnValue.setUsers(users);
		return returnValue;
	}

	public OrderItemDto cartItemToOrderItem(CartItemDto cartItem) {
		OrderItemDto returnValue = mapper.map(cartItem, OrderItemDto.class);

		Optional<Integer> productIdOptional = Optional.ofNullable(cartItem.getProductId());
		if (productIdOptional.isPresent()) {
			Integer productId = productIdOptional.get();
			ProductEntity product = productRepository.findById(productId).orElse(null);
			if (product != null) {
				returnValue.setProductName(product.getProductName());
				returnValue.setProductPrice(product.getProductPrice());
			}

		}

		return returnValue;
	}

	public AddressDto addressEntityToDto(AddressEntity addressEntity) {
		AddressDto returnValue = mapper.map(addressEntity, AddressDto.class);

		Optional<CustomerEntity> customerOptional = Optional.ofNullable(addressEntity.getCustomer());
		if (customerOptional.isPresent()) {
			returnValue.setCustomerId(customerOptional.get().getCustomerId());
		}

		return returnValue;
	}

	public AddressEntity addressDtoToEntity(AddressDto address) {
		AddressEntity returnValue = mapper.map(address, AddressEntity.class);

		Optional<Integer> customerIdOptional = Optional.ofNullable(address.getCustomerId());
		if (customerIdOptional.isPresent()) {
			Integer customerId = customerIdOptional.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).orElse(null);
			if (customerEntity != null) {
				returnValue.setCustomer(customerEntity);
			}

		}

		return returnValue;
	}

	public OrderAddressDto orderAddressEntityToDto(OrderAddressEntity address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		Optional<OrderEntity> orderOptional = Optional.ofNullable(address.getOrder());
		if (orderOptional.isPresent()) {
			returnValue.setOrderId(orderOptional.get().getOrderId());
		}

		return returnValue;
	}

	public OrderAddressEntity orderAddressDtoToEntity(OrderAddressDto address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		Optional<Integer> orderIdOptional = Optional.ofNullable(address.getOrderId());
		if (orderIdOptional.isPresent()) {
			Integer orderId = orderIdOptional.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
			if (orderEntity != null) {
				returnValue.setOrder(orderEntity);
			}
		}

		return returnValue;
	}

	public OrderAddressDto addressToOrderAddress(AddressDto address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		return returnValue;
	}
}
