package com.radovan.spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.AddressDto;
import com.radovan.spring.entity.AddressEntity;
import com.radovan.spring.repository.AddressRepository;
import com.radovan.spring.service.AddressService;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public AddressDto getAddressById(Integer addressId) {
		// TODO Auto-generated method stub
		AddressDto returnValue = null;
		Optional<AddressEntity> addressOpt = addressRepository.findById(addressId);
		if (addressOpt.isPresent()) {
			returnValue = tempConverter.addressEntityToDto(addressOpt.get());
		}
		return returnValue;
	}

	@Override
	public AddressDto createAddress(AddressDto address) {
		// TODO Auto-generated method stub
		AddressEntity addressEntity = tempConverter.addressDtoToEntity(address);
		AddressEntity storedAddress = addressRepository.save(addressEntity);
		AddressDto returnValue = tempConverter.addressEntityToDto(storedAddress);
		return returnValue;
	}

	@Override
	public void deleteAddress(Integer addressId) {
		// TODO Auto-generated method stub
		addressRepository.deleteById(addressId);
		addressRepository.flush();
	}
}
