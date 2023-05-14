package com.radovan.spring.service.impl;

import java.text.DecimalFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.service.CartService;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private TempConverter tempConverter;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	@Override
	public CartDto getCartByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		CartDto returnValue = null;
		Optional<CartEntity> cartOpt = cartRepository.findById(cartId);
		if (cartOpt.isPresent()) {
			returnValue = tempConverter.cartEntityToDto(cartOpt.get());
		}
		return returnValue;
	}

	@Override
	public Float calculateCartPrice(Integer cartId) {
		// TODO Auto-generated method stub
		Float returnValue = 0f;
		Optional<Float> cartPriceOpt = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId));
		if (cartPriceOpt.isPresent()) {
			returnValue = cartPriceOpt.get();
			returnValue = Float.valueOf(decfor.format(returnValue));
		}
		return returnValue;
	}

	@Override
	public void refreshCartState(Integer cartId) {
		// TODO Auto-generated method stub
		CartEntity cartEntity = cartRepository.findById(cartId).get();
		Optional<Float> priceOpt = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId));
		if (priceOpt.isPresent()) {
			Float price = priceOpt.get();
			price = Float.valueOf(decfor.format(price));
			cartEntity.setCartPrice(price);
		} else {
			cartEntity.setCartPrice(0f);
		}
		cartRepository.saveAndFlush(cartEntity);

	}

	@Override
	public CartDto validateCart(Integer cartId) {
		// TODO Auto-generated method stub
		Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
		CartDto returnValue = null;
		Error error = new Error("Invalid Cart");

		if (cartEntity.isPresent()) {
			if (cartEntity.get().getCartItems().size() == 0) {
				throw new InvalidCartException(error);
			}

			returnValue = tempConverter.cartEntityToDto(cartEntity.get());

		} else {
			throw new InvalidCartException(error);
		}

		return returnValue;
	}

	@Override
	public void deleteCart(Integer cartId) {
		// TODO Auto-generated method stub
		cartRepository.deleteById(cartId);
		cartItemRepository.flush();
	}
}
