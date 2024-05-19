package com.radovan.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.service.OrderAddressService;

@Service
public class OrderAddressServiceImpl implements OrderAddressService {

	@Autowired
	private OrderAddressRepository addressRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	@Transactional(readOnly = true)
	public OrderAddressDto getAddressById(Integer addressId) {
		// TODO Auto-generated method stub

		OrderAddressEntity addressEntity = addressRepository.findById(addressId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The address has not been found!")));
		return tempConverter.orderAddressEntityToDto(addressEntity);
	}

	@Override
	@Transactional
	public void deleteAddress(Integer addressId) {
		// TODO Auto-generated method stub
		addressRepository.deleteById(addressId);
		addressRepository.flush();
	}
}
