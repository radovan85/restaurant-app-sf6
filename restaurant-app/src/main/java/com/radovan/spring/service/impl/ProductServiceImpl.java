package com.radovan.spring.service.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.ProductEntity;
import com.radovan.spring.exceptions.InstanceUndefinedException;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.ProductRepository;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemRepository cartItemRepository;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	@Override
	@Transactional(readOnly = true)
	public ProductDto getProduct(Integer productId) {
		// TODO Auto-generated method stub
		ProductEntity productEntity = productRepository.findById(productId)
				.orElseThrow(() -> new InstanceUndefinedException(new Error("The product has not been found!")));
		return tempConverter.productEntityToDto(productEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductDto> listByCategory(String category) {
		// TODO Auto-generated method stub
		List<ProductEntity> allProducts = productRepository.findAllByCategory(category);
		return allProducts.stream().map(tempConverter::productEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ProductDto addProduct(ProductDto product) {
		// TODO Auto-generated method stub
		Optional<Integer> productIdOpt = Optional.ofNullable(product.getProductId());
		ProductEntity productEntity = tempConverter.productDtoToEntity(product);
		ProductEntity storedProduct = productRepository.save(productEntity);
		ProductDto returnValue = tempConverter.productEntityToDto(storedProduct);

		if (productIdOpt.isPresent()) {
			List<CartItemEntity> allCartItems = cartItemRepository.findAllByProductId(returnValue.getProductId());
			allCartItems.forEach((itemEntity) -> {
				Float price = returnValue.getProductPrice();
				price = price * itemEntity.getQuantity();
				price = Float.valueOf(decfor.format(price));
				itemEntity.setPrice(price);
				cartItemRepository.saveAndFlush(itemEntity);
			});

			cartService.refreshAllCarts();
		}
		return returnValue;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductDto> listAll() {
		// TODO Auto-generated method stub
		List<ProductEntity> allProducts = productRepository.findAll();
		return allProducts.stream().map(tempConverter::productEntityToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteProduct(Integer productId) {
		// TODO Auto-generated method stub
		productRepository.deleteById(productId);
		productRepository.flush();
	}

}
