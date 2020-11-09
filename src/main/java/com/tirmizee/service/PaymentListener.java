package com.tirmizee.service;

import org.springframework.stereotype.Service;

import com.tirmizee.model.Payment;

@Service
public class PaymentListener {
	
	public void handleMessage(Payment payment) {
		System.out.println(String.valueOf(payment));
	}

}
