package com.tirmizee.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageListenerImpl implements RedisMessageListener {

	@Override
	public void onMessage(Message message, byte[] pattern) {
		System.out.println("Message received: " + message.toString() + ", " + new String(pattern));		
	}

}
