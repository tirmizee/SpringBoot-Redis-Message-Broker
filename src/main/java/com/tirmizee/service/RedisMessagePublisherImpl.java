package com.tirmizee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisherImpl implements RedisMessagePublisher {

	@Autowired
    private ChannelTopic topic;
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void publishMessage(String message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);		
	}
	
}
