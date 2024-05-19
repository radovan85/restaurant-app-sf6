package com.radovan.spring.service.impl;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private TempConverter tempConverter;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	@Override
	@Transactional(readOnly = true)
	public CartDto getCartByCartId(Integer cartId) {
		// TODO Auto-generated method stub

		CartEntity cartEntity = cartRepository.findById(cartId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The cart has not been found!")));
		return tempConverter.cartEntityToDto(cartEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Float calculateCartPrice(Integer cartId) {
		// TODO Auto-generated method stub

		return cartItemRepository.calculateGrandTotal(cartId).orElse(0f);
	}

	@Override
	@Transactional
	public void refreshCartState(Integer cartId) {
		// TODO Auto-generated method stub
		CartDto cart = getCartByCartId(cartId);
		Float price = cartItemRepository.calculateGrandTotal(cartId).orElse(0f);
		cart.setCartPrice(Float.valueOf(decfor.format(price)));
		cartRepository.saveAndFlush(tempConverter.cartDtoToEntity(cart));

	}

	@Override
	@Transactional(readOnly = true)
	public CartDto validateCart(Integer cartId) {
		// TODO Auto-generated method stub
		CartDto cart = getCartByCartId(cartId);
		if (cart.getCartItemsIds().isEmpty()) {
			throw new InvalidCartException(new Error("Invalid cart!"));
		}

		return cart;
	}

	@Override
	@Transactional
	public void refreshAllCarts() {
		// TODO Auto-generated method stub
		List<CartEntity> allCarts = cartRepository.findAll();
		allCarts.forEach((cart) -> {
			refreshCartState(cart.getCartId());
		});
	}

}
