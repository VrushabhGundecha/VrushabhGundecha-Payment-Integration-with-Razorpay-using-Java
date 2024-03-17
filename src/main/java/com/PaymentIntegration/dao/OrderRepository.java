package com.PaymentIntegration.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PaymentIntegration.entities.MyOrder;

public interface OrderRepository extends JpaRepository<MyOrder, Long>{

	public MyOrder findByOrderID(String orderID);
	 
}
