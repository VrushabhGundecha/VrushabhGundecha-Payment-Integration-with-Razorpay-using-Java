package com.PaymentIntegration.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.PaymentIntegration.dao.OrderRepository;
import com.PaymentIntegration.entities.MyOrder;
import com.razorpay.*;

@Controller
public class HomeController {

	@Autowired
	private OrderRepository orderRepository;
	
	@GetMapping("/donate")
	public String initiatePayment() {
		return "donate";
	}
	
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data) throws Exception {
		
		//System.out.println("This is order function executed");
		System.out.println(data);
		
		// get amount in string format and then convert to Integer format
		int amt = Integer.parseInt(data.get("amount").toString());
		
		RazorpayClient client = new RazorpayClient("rzp_test_tVBRqSf0PDIupW", "Dgq6oqoivw2VV4zEn6avBP4F");
		
		
		//	Creating JSON object
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", amt*100);	//	convert amount in paise
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "txn_101");
		
		//	creating new order
		Order createOrder = client.orders.create(orderRequest);
		System.out.println(createOrder);
		
		//	Save order to database
		MyOrder order = new MyOrder();
		
		order.setAmount(String.valueOf((int)createOrder.get("amount")/100));
		order.setCurrency(createOrder.get("currency"));
		order.setOrderID(createOrder.get("id"));
		order.setPaymentId(null);
		order.setReceipt(createOrder.get("receipt"));
		order.setStatus(createOrder.get("status"));
		
		this.orderRepository.save(order);
		
		return createOrder.toString();
	}
	
	@PostMapping("/update_server")
	@ResponseBody
	public String updateOrder(@RequestBody Map<String, Object> orderData){
		
		MyOrder myOrder = this.orderRepository.findByOrderID(orderData.get("order_id").toString());
		
		myOrder.setPaymentId(orderData.get("payment_id").toString());
		myOrder.setStatus(orderData.get("status").toString());
		
		this.orderRepository.save(myOrder);
		
		System.out.println("Payment Done : " + myOrder); 
		
		return "updated";
	}
	
}




