package com.radovan.spring.entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "cart_items")
public class CartItemEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "item_id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer cartItemId;

	@Column(nullable = false)
	private Float price;

	@Column(nullable = false)
	private Integer quantity;

	@Column(name = "hotness_level", nullable = false)
	private String hotnessLevel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cart_id", nullable = false)
	private CartEntity cart;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = false)
	private ProductEntity product;

	@Transient
	private List<String> hotnessLevelList;

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

	public CartEntity getCart() {
		return cart;
	}

	public void setCart(CartEntity cart) {
		this.cart = cart;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public List<String> getHotnessLevelList() {
		return hotnessLevelList;
	}

	public void setHotnessLevelList(List<String> hotnessLevelList) {
		this.hotnessLevelList = hotnessLevelList;
	}

}
