package com.radovan.spring.service;

import com.radovan.spring.dto.AddressDto;

public interface AddressService {

	AddressDto getAddressById(Integer addressId);
	
	AddressDto createAddress(AddressDto address);
	
	void deleteAddress(Integer addressId);
}
