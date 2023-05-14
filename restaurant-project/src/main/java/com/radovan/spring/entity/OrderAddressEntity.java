package com.radovan.spring.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_addresses")
public class OrderAddressEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer orderAddressId;

	@Column(nullable = false, length = 75)
	private String address;

	@Column(nullable = false, length = 40)
	private String city;

	@Column(name = "post_code", nullable = false, length = 10)
	private String postcode;

	@OneToOne(mappedBy = "address", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private OrderEntity order;

	public Integer getOrderAddressId() {
		return orderAddressId;
	}

	public void setOrderAddressId(Integer orderAddressId) {
		this.orderAddressId = orderAddressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

}
