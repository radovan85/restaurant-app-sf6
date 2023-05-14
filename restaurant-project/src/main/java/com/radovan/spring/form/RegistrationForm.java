package com.radovan.spring.form;

import java.io.Serializable;

import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.UserDto;

public class RegistrationForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UserDto user;
	
	private CustomerDto customer;
	
	private AddressDto address;

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public CustomerDto getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}

	public AddressDto getAddress() {
		return address;
	}

	public void setAddress(AddressDto address) {
		this.address = address;
	}
	
	

}
