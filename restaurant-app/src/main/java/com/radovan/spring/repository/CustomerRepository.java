package com.radovan.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

	@Query(value = "select * from customers where cart_id = :cartId", nativeQuery = true)
	Optional<CustomerEntity> findByCartId(@Param("cartId") Integer cartId);

	@Query(value = "select * from customers where user_id = :userId", nativeQuery = true)
	Optional<CustomerEntity> findByUserId(@Param("userId") Integer userId);

}
