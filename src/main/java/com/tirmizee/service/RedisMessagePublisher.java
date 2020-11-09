package com.tirmizee.service;

import com.tirmizee.model.Payment;

public interface RedisMessagePublisher {
	
	 public void publishMessage(String message);
	 
	 public void publishToPaymentChanel(Payment payment);

}
