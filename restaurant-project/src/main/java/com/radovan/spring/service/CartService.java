package com.radovan.spring.service;

import com.radovan.spring.dto.CartDto;

public interface CartService {

	CartDto getCartByCartId(Integer cartId);

	Float calculateCartPrice(Integer cartId);

	void refreshCartState(Integer cartId);

	CartDto validateCart(Integer cartId);
	
	void deleteCart(Integer cartId);
}
