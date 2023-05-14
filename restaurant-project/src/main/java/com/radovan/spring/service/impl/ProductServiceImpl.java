package com.radovan.spring.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.ProductEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.ProductRepository;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	@Override
	public ProductDto getProduct(Integer productId) {
		// TODO Auto-generated method stub
		Optional<ProductEntity> productOpt = productRepository.findById(productId);
		ProductDto returnValue = null;
		if (productOpt.isPresent()) {
			returnValue = tempConverter.productEntityToDto(productOpt.get());
		}
		return returnValue;
	}

	@Override
	public List<ProductDto> listByCategory(String category) {
		// TODO Auto-generated method stub
		Optional<List<ProductEntity>> allProductsOpt = Optional
				.ofNullable(productRepository.findAllByCategory(category));
		List<ProductDto> returnValue = new ArrayList<ProductDto>();
		if (!allProductsOpt.isEmpty()) {
			allProductsOpt.get().forEach((product) -> {
				ProductDto productDto = tempConverter.productEntityToDto(product);
				returnValue.add(productDto);
			});
		}
		return returnValue;
	}

	@Override
	public ProductDto addProduct(ProductDto product) {
		// TODO Auto-generated method stub
		Optional<Integer> productIdOpt = Optional.ofNullable(product.getProductId());
		ProductEntity productEntity = tempConverter.productDtoToEntity(product);
		ProductEntity storedProduct = productRepository.save(productEntity);
		ProductDto returnValue = tempConverter.productEntityToDto(storedProduct);

		if (productIdOpt.isPresent()) {
			Optional<List<CartItemEntity>> allCartItems = Optional
					.ofNullable(cartItemRepository.findAllByProductId(returnValue.getProductId()));
			if (!allCartItems.isEmpty()) {
				allCartItems.get().forEach((itemEntity) -> {
					Float price = returnValue.getProductPrice();
					price = price * itemEntity.getQuantity();
					price = Float.valueOf(decfor.format(price));
					itemEntity.setPrice(price);
					cartItemRepository.saveAndFlush(itemEntity);
				});

				Optional<List<CartEntity>> allCartsOpt = Optional.ofNullable(cartRepository.findAll());
				if (!allCartsOpt.isEmpty()) {
					allCartsOpt.get().forEach((cartEntity) -> {
						cartService.refreshCartState(cartEntity.getCartId());
					});
				}
			}
		}
		return returnValue;
	}

	@Override
	public List<ProductDto> listAll() {
		// TODO Auto-generated method stub
		Optional<List<ProductEntity>> allProductsOpt = Optional.ofNullable(productRepository.findAll());
		List<ProductDto> returnValue = new ArrayList<ProductDto>();

		if (!allProductsOpt.isEmpty()) {
			allProductsOpt.get().forEach((productEntity) -> {
				ProductDto productDto = tempConverter.productEntityToDto(productEntity);
				returnValue.add(productDto);
			});
		}
		return returnValue;
	}

	@Override
	public void deleteProduct(Integer productId) {
		// TODO Auto-generated method stub
		productRepository.deleteById(productId);
		productRepository.flush();
	}

}
