package com.radovan.spring.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

	@Query(value = "select * from orders where cart_id = :cartId", nativeQuery = true)
	List<OrderEntity> findAllByCartId(@Param("cartId") Integer customerId);

	@Query(value = "select * from orders where created_at >= :timestamp1 and created_at <= :timestamp2", nativeQuery = true)
	List<OrderEntity> findAllTodaysOrders(@Param("timestamp1") Timestamp timestamp1,
			@Param("timestamp2") Timestamp timestamp2);

}
