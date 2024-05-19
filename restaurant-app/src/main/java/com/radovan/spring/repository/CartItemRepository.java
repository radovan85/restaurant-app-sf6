package com.radovan.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.CartItemEntity;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {

	@Query(value = "select * from cart_items where cart_id = :cartId", nativeQuery = true)
	List<CartItemEntity> findAllByCartId(@Param("cartId") Integer cartId);

	@Query(value = "select * from cart_items where product_id = :productId", nativeQuery = true)
	List<CartItemEntity> findAllByProductId(@Param("productId") Integer productId);

	@Query(value = "select sum(price) from cart_items where cart_id = :cartId", nativeQuery = true)
	Optional<Float> calculateGrandTotal(@Param("cartId") Integer cartId);

	@Modifying
	@Query(value = "delete from cart_items where cart_id = :cartId", nativeQuery = true)
	void removeAllByCartId(@Param("cartId") Integer cartId);

	@Modifying
	@Query(value = "delete from cart_items where item_id = :itemId", nativeQuery = true)
	void removeCartItem(@Param("itemId") Integer itemId);

	@Query(value = "select * from cart_items where cart_id = :cartId and product_id = :productId and hotness_level = :hotnessLevel", nativeQuery = true)
	Optional<CartItemEntity> findByCartIdAndProductIdAndHotnessLevel(@Param("cartId") Integer cartId,
			@Param("productId") Integer productId, @Param("hotnessLevel") String hotnessLevel);

	@Modifying
	@Query(value = "delete from cart_items where product_id = :productId", nativeQuery = true)
	void removeAllByProductId(@Param("productId") Integer productId);
}
