package com.radovan.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {

	@Query(value = "select * from order_items where order_id = :orderId", nativeQuery = true)
	List<OrderItemEntity> findAllByOrderId(@Param("orderId") Integer orderId);

	@Query(value = "select sum(price) from order_items where order_id = :orderId", nativeQuery = true)
	Optional<Float> calculateGrandTotal(@Param("orderId") Integer orderId);

}
