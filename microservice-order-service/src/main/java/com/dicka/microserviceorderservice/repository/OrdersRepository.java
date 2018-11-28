package com.dicka.microserviceorderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dicka.microserviceorderservice.entity.Order;

public interface OrdersRepository extends JpaRepository<Order, Long>{

	Order findOrderById(Long id);
	
}
