package com.radovan.spring.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.ProductService;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProductService productService;

	@Override
	@Transactional
	public CartItemDto addCartItem(CartItemDto cartItem) {
		// TODO Auto-generated method stub
		CartItemDto returnValue = null;
		Integer productId = cartItem.getProductId();
		String hotnessLevel = cartItem.getHotnessLevel();
		CustomerDto customer = customerService.getCurrentCustomer();
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		ProductDto product = productService.getProduct(productId);

		CartItemEntity existingCartItem = cartItemRepository
				.findByCartIdAndProductIdAndHotnessLevel(cart.getCartId(), productId, hotnessLevel).orElse(null);
		if (existingCartItem != null) {
			System.out.println("Existing item");
			cartItem.setCartItemId(existingCartItem.getCartItemId());
			cartItem.setCartId(cart.getCartId());
			cartItem.setQuantity(existingCartItem.getQuantity() + cartItem.getQuantity());
			if (cartItem.getQuantity() > 50) {
				cartItem.setQuantity(50);
			}
			cartItem.setPrice(product.getProductPrice() * cartItem.getQuantity());
			CartItemEntity cartItemEntity = tempConverter.cartItemDtoToEntity(cartItem);
			CartItemEntity updatedItem = cartItemRepository.saveAndFlush(cartItemEntity);
			returnValue = tempConverter.cartItemEntityToDto(updatedItem);
			cartService.refreshCartState(cart.getCartId());
		} else {
			cartItem.setQuantity(cartItem.getQuantity());
			if (cartItem.getQuantity() > 50) {
				cartItem.setQuantity(50);
			}
			cartItem.setCartId(cart.getCartId());
			cartItem.setPrice(product.getProductPrice() * cartItem.getQuantity());
			CartItemEntity cartItemEntity = tempConverter.cartItemDtoToEntity(cartItem);
			CartItemEntity storedItem = cartItemRepository.save(cartItemEntity);
			returnValue = tempConverter.cartItemEntityToDto(storedItem);
			cartService.refreshCartState(cart.getCartId());
		}
		return returnValue;
	}

	@Override
	@Transactional
	public void removeCartItem(Integer itemId) {
		// TODO Auto-generated method stub
		CartItemDto item = getCartItem(itemId);
		Integer cartId = item.getCartId();
		cartItemRepository.removeCartItem(itemId);
		cartItemRepository.flush();
		cartService.refreshCartState(cartId);

	}

	@Override
	@Transactional
	public void eraseAllCartItems(Integer cartId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByCartId(cartId);
		cartItemRepository.flush();
		cartService.refreshCartState(cartId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CartItemDto> listAllByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		List<CartItemEntity> allItems = cartItemRepository.findAllByCartId(cartId);
		return allItems.stream().map(tempConverter::cartItemEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public CartItemDto getCartItem(Integer id) {
		// TODO Auto-generated method stub
		CartItemEntity itemEntity = cartItemRepository.findById(id).orElse(null);
		return tempConverter.cartItemEntityToDto(itemEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public CartItemDto getCartItemByCartIdAndProductIdAndHotnessLevel(Integer cartId, Integer productId,
			String hotnessLevel) {
		// TODO Auto-generated method stub
		CartItemEntity itemEntity = cartItemRepository
				.findByCartIdAndProductIdAndHotnessLevel(cartId, productId, hotnessLevel).orElse(null);
		return tempConverter.cartItemEntityToDto(itemEntity);
	}

	@Override
	@Transactional
	public void eraseAllByProductId(Integer productId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByProductId(productId);
		cartItemRepository.flush();
		cartService.refreshAllCarts();
	}

}
