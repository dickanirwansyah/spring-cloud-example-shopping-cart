package com.dicka.microserviceorderservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dicka.microserviceorderservice.GenericData;
import com.dicka.microserviceorderservice.ResourceNotFound;
import com.dicka.microserviceorderservice.entity.Order;
import com.dicka.microserviceorderservice.repository.OrdersRepository;

@RestController
public class OrdersController {

	private final OrdersRepository ordersRepository;
	
	@Autowired
	public OrdersController(OrdersRepository ordersRepository) {
		this.ordersRepository = ordersRepository;
	}
	
	@PostMapping(value = "/api/orders")
	public Order createOrder(@RequestBody Order order) {
		return ordersRepository.save(order);
	}
	
	@GetMapping(value = "/api/orders/{orderId}")
	public Optional<Order> findOrderById(@PathVariable("orderId") Long orderId){
		return ordersRepository.findById(orderId);
	}
	
	public ResponseEntity<Order> post(Long id, @RequestBody Order order){
		return ordersRepository.findById(id)
				.map(data -> {
					data.setCustomerAddress(order.getCustomerAddress());
					return ResponseEntity.ok(
							ordersRepository.save(data)
						);
				}).orElseThrow(()-> new ResourceNotFound("test"));
	}
	
	public GenericData gets(Long id){
		Order order = getIdss(id);
		return new GenericData(order.getId(), 
				order.getCustomerEmail());
	}
	
	private Order getIdss(Long id) {
		return ordersRepository.findOrderById(id);
	}
}
