package com.radovan.spring.service;

import com.radovan.spring.dto.CartDto;

public interface CartService {

	CartDto getCartByCartId(Integer cartId);

	Float calculateCartPrice(Integer cartId);

	void refreshCartState(Integer cartId);

	void refreshAllCarts();

	CartDto validateCart(Integer cartId);

}
