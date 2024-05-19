package com.radovan.spring.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItemEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "item_id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer orderItemId;

	@Column(nullable = false)
	private Float price;

	@Column(nullable = false)
	private Integer quantity;

	@Column(name = "hotness_level", nullable = false)
	private String hotnessLevel;

	@Column(name = "product_name", length = 40, nullable = false)
	private String productName;

	@Column(name = "product_price", nullable = false)
	private Float productPrice;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private OrderEntity order;

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
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

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Float productPrice) {
		this.productPrice = productPrice;
	}

}
