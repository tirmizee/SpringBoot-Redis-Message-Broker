package com.tirmizee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.tirmizee.model.Payment;

@Service
public class RedisMessagePublisherImpl implements RedisMessagePublisher {

	@Autowired
    private ChannelTopic topic;
	
	@Autowired
    private ChannelTopic topicPayment;
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
    private RedisTemplate<String, Payment> redisTemplatePayment;

	@Override
	public void publishMessage(String message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);		
	}

	@Override
	public void publishToPaymentChanel(Payment payment) {
		redisTemplatePayment.convertAndSend(topicPayment.getTopic(), payment);		
	}
	
}
