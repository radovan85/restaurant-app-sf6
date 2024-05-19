package com.radovan.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	@Transactional
	public UserDto updateUser(Integer id, UserDto user) {
		// TODO Auto-generated method stub
		getUserById(id);
		user.setId(id);
		UserEntity userEntity = tempConverter.userDtoToEntity(user);
		UserEntity updatedUser = userRepository.saveAndFlush(userEntity);
		return tempConverter.userEntityToDto(updatedUser);
	}

	@Override
	@Transactional
	public void deleteUser(Integer id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);
		userRepository.flush();
	}

	@Override
	@Transactional
	public UserDto getUserById(Integer id) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findById(id)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("Invalid user!")));

		return tempConverter.userEntityToDto(userEntity);
	}

	@Override
	@Transactional
	public List<UserDto> listAllUsers() {
		// TODO Auto-generated method stub
		List<UserEntity> allUsers = userRepository.findAll();
		return allUsers.stream().map(tempConverter::userEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub

		UserEntity userEntity = userRepository.findByEmail(email)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("Invalid user!")));

		return tempConverter.userEntityToDto(userEntity);
	}

	@Override
	@Transactional
	public UserDto getCurrentUser() {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return getUserById(authUser.getId());
	}

	@Override
	@Transactional
	public void suspendUser(Integer userId) {
		// TODO Auto-generated method stub
		UserDto user = getUserById(userId);
		user.setEnabled((short) 0);
		userRepository.saveAndFlush(tempConverter.userDtoToEntity(user));
	}

	@Override
	@Transactional
	public void clearSuspension(Integer userId) {
		// TODO Auto-generated method stub
		UserDto user = getUserById(userId);
		user.setEnabled((short) 1);
		userRepository.saveAndFlush(tempConverter.userDtoToEntity(user));
	}
}
