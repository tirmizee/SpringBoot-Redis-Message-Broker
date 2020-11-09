package com.tirmizee.config;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

	@Value(value = "${spring.redis.host}")
	private String host;
	
	@Value(value = "${spring.redis.port}")
	private int port;
	
	@Value(value = "${spring.redis.password}")
	private String password;
	
	@Value(value = "${spring.redis.jedis.pool.min-idle}")
	private int minIdle;
	
	@Value(value = "${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;
	
	@Value(value = "${spring.redis.jedis.pool.max-active}")
	private int maxActive;
	
	@Bean
	public JedisPool jedisPool() {
		GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
		poolConfig.setMaxTotal(maxActive);
		poolConfig.setMinIdle(minIdle);
		poolConfig.setMaxIdle(maxIdle);
		return new JedisPool(poolConfig, "localhost");
	}
	
	@Bean
	public RedisStandaloneConfiguration standaloneConfiguration() {
		RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
		standaloneConfiguration.setHostName(host);
		standaloneConfiguration.setPassword(password);
		standaloneConfiguration.setPort(port);
		return standaloneConfiguration;
	}
	
	@Bean
	public JedisConnectionFactory jedisConnectionFactory(JedisPool jedisPool, RedisStandaloneConfiguration standaloneConfiguration) {
		JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder()
			.connectTimeout(Duration.ofSeconds(3))
			.readTimeout(Duration.ofSeconds(3))
			.usePooling()
			.build();
		return new JedisConnectionFactory(standaloneConfiguration, clientConfiguration);
	}
	
}
