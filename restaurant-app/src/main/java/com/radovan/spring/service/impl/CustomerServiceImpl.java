package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.AddressEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.form.RegistrationForm;
import com.radovan.spring.repository.AddressRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.UserService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Override
	@Transactional
	public CustomerDto addCustomer(CustomerDto customer) {
		// TODO Auto-generated method stub
		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customer);
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);
		CustomerDto returnValue = tempConverter.customerEntityToDto(storedCustomer);
		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCustomer(Integer customerId) {
		// TODO Auto-generated method stub
		CustomerEntity customerEntity = customerRepository.findById(customerId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The customer has not been found!")));
		return tempConverter.customerEntityToDto(customerEntity);

	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCustomerByUserId(Integer userId) {
		// TODO Auto-generated method stub
		CustomerEntity customerEntity = customerRepository.findByUserId(userId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The customer has not been found!")));
		return tempConverter.customerEntityToDto(customerEntity);

	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomerDto> listAll() {
		// TODO Auto-generated method stub
		List<CustomerEntity> allCustomers = customerRepository.findAll();
		return allCustomers.stream().map(tempConverter::customerEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public CustomerDto registerCustomer(RegistrationForm form) {
		// TODO Auto-generated method stub

		UserDto userDto = form.getUser();
		Optional<UserEntity> testUser = userRepository.findByEmail(userDto.getEmail());
		if (testUser.isPresent()) {
			Error error = new Error("Email exists");
			throw new ExistingEmailException(error);
		}

		RoleEntity role = roleRepository.findByRole("ROLE_USER").orElse(null);
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userDto.setEnabled((short) 1);
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(role);
		UserEntity userEntity = tempConverter.userDtoToEntity(userDto);
		userEntity.setRoles(roles);
		userEntity.setEnabled((byte) 1);
		UserEntity storedUser = userRepository.save(userEntity);
		List<UserEntity> users = new ArrayList<UserEntity>();
		users.add(storedUser);
		role.setUsers(users);
		roleRepository.saveAndFlush(role);

		AddressDto address = form.getAddress();
		AddressEntity addressEntity = tempConverter.addressDtoToEntity(address);
		AddressEntity storedAddress = addressRepository.save(addressEntity);

		CartEntity cartEntity = new CartEntity();
		cartEntity.setCartPrice(0f);
		CartEntity storedCart = cartRepository.save(cartEntity);

		CustomerDto customerDto = form.getCustomer();
		customerDto.setUserId(storedUser.getId());
		customerDto.setCartId(storedCart.getCartId());
		customerDto.setAddressId(storedAddress.getAddressId());
		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customerDto);
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);

		storedCart.setCustomer(storedCustomer);
		cartRepository.saveAndFlush(storedCart);

		storedAddress.setCustomer(storedCustomer);
		addressRepository.saveAndFlush(storedAddress);

		CustomerDto returnValue = tempConverter.customerEntityToDto(storedCustomer);
		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCustomerByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		CustomerEntity customerEntity = customerRepository.findByCartId(cartId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The customer has not been found!")));
		return tempConverter.customerEntityToDto(customerEntity);
	}

	@Override
	@Transactional
	public void deleteCustomer(Integer customerId) {
		// TODO Auto-generated method stub
		List<OrderDto> orders = orderService.listAllByCustomerId(customerId);
		orders.forEach((order) -> {
			orderService.deleteOrder(order.getOrderId());
		});
		customerRepository.deleteById(customerId);
		customerRepository.flush();
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCurrentCustomer() {
		// TODO Auto-generated method stub
		UserDto authUser = userService.getCurrentUser();
		return getCustomerByUserId(authUser.getId());
	}

}
