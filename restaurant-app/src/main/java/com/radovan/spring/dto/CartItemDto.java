package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartItemDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer cartItemId;

	private Float price;

	private Integer quantity;

	private String hotnessLevel;

	private Integer cartId;

	private Integer productId;

	private List<String> hotnessLevelList;

	public CartItemDto() {
		hotnessLevelList = new ArrayList<String>();
		hotnessLevelList.add("Extremly Cold");
		hotnessLevelList.add("Cold");
		hotnessLevelList.add("Warmish");
		hotnessLevelList.add("Warm");
		hotnessLevelList.add("Hot");
		hotnessLevelList.add("Extremly Hot");
	}

	public Integer getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Integer cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getHotnessLevel() {
		return hotnessLevel;
	}

	public void setHotnessLevel(String hotnessLevel) {
		this.hotnessLevel = hotnessLevel;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public List<String> getHotnessLevelList() {
		return hotnessLevelList;
	}

	public void setHotnessLevelList(List<String> hotnessLevelList) {
		this.hotnessLevelList = hotnessLevelList;
	}

	
	
	

}
