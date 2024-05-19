package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.ProductService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@GetMapping(value = "/addToCart/{productId}")
	public String renderItemForm(@PathVariable("productId") Integer productId, ModelMap map) {

		CartItemDto cartItem = new CartItemDto();
		ProductDto selectedProduct = productService.getProduct(productId);
		map.put("cartItem", cartItem);
		map.put("selectedProduct", selectedProduct);
		map.put("allHotnessLevels", cartItem.getHotnessLevelList());
		return "fragments/cartItemForm :: fragmentContent";
	}

	@PostMapping(value = "/addToCart")
	public String addCartItem(@ModelAttribute("cartItem") CartItemDto cartItem) {
		cartItemService.addCartItem(cartItem);
		return "fragments/homePage :: fragmentContent";

	}

	@GetMapping(value = "/itemAddCompleted")
	public String itemAdded() {
		return "fragments/itemAdded :: fragmentContent";
	}

	@GetMapping(value = "/getCart")
	public String cartDetails(ModelMap map) {
		CustomerDto customer = customerService.getCurrentCustomer();
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(cart.getCartId());
		List<ProductDto> allProducts = productService.listAll();
		map.put("allCartItems", allCartItems);
		map.put("allProducts", allProducts);
		map.put("cart", cart);
		return "fragments/cart :: fragmentContent";

	}

	@GetMapping(value = "/deleteItem/{itemId}")
	public String deleteCartItem(@PathVariable("itemId") Integer itemId) {
		cartItemService.removeCartItem(itemId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/deleteAllItems/{cartId}")
	public String deleteAllCartItems(@PathVariable("cartId") Integer cartId) {
		cartItemService.eraseAllCartItems(cartId);
		return "fragments/homePage :: fragmentContent";
	}

	@GetMapping(value = "/invalidCart")
	public String invalidCartEx(ModelMap map) {
		return "fragments/invalidCart :: fragmentContent";
	}
}
