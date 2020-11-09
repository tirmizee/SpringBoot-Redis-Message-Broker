package com.tirmizee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.tirmizee.service.RedisMessagePublisher;

@SpringBootApplication
public class SpringBootRedisMessageBrokerApplication implements CommandLineRunner {

	@Autowired
	ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisMessageBrokerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		RedisMessagePublisher publisher = applicationContext.getBean(RedisMessagePublisher.class);
		publisher.publishMessage("Hello world.");
	}

}
