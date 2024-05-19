package com.radovan.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.entity.AddressEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.AddressRepository;
import com.radovan.spring.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	@Transactional(readOnly = true)
	public AddressDto getAddressById(Integer addressId) {
		// TODO Auto-generated method stub

		AddressEntity addressEntity = addressRepository.findById(addressId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The address has not been found!")));
		return tempConverter.addressEntityToDto(addressEntity);
	}

	@Override
	@Transactional
	public AddressDto createAddress(AddressDto address) {
		// TODO Auto-generated method stub
		AddressEntity addressEntity = tempConverter.addressDtoToEntity(address);
		AddressEntity storedAddress = addressRepository.save(addressEntity);
		AddressDto returnValue = tempConverter.addressEntityToDto(storedAddress);
		return returnValue;
	}

	@Override
	@Transactional
	public void deleteAddress(Integer addressId) {
		// TODO Auto-generated method stub
		addressRepository.deleteById(addressId);
		addressRepository.flush();
	}
}
