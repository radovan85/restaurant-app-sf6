package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.form.RegistrationForm;

public interface CustomerService {

	CustomerDto addCustomer(CustomerDto customer);

	CustomerDto getCustomer(Integer customerId);

	CustomerDto getCustomerByUserId(Integer userId);

	List<CustomerDto> listAll();

	CustomerDto registerCustomer(RegistrationForm form);

	CustomerDto getCustomerByCartId(Integer cartId);

	void deleteCustomer(Integer customerId);

	void resetCustomer(Integer customerId);
}
