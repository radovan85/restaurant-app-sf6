package com.radovan.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	@Transactional(readOnly = true)
	public List<RoleDto> listAllAuthorities() {
		// TODO Auto-generated method stub
		List<RoleEntity> allRoles = roleRepository.findAll();
		return allRoles.stream().map(tempConverter::roleEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public RoleDto getRoleById(Integer id) {
		// TODO Auto-generated method stub
		RoleEntity roleEntity = roleRepository.findById(id)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The role has not been found")));
		return tempConverter.roleEntityToDto(roleEntity);
	}
}
