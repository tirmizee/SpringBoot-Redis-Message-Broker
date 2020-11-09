package com.tirmizee.config;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.tirmizee.model.Payment;
import com.tirmizee.service.PaymentListener;
import com.tirmizee.service.RedisMessageListener;

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
	public ChannelTopic topic() {
	    return new ChannelTopic("messageQueue");
	}
	
	@Bean
	public ChannelTopic topicPayment() {
	    return new ChannelTopic("paymentQueue");
	}
	
	@Bean
	public RedisSerializer<Payment> paymentSerializer(){
		return new Jackson2JsonRedisSerializer<>(Payment.class);
	}
	
	@Bean
    public MessageListenerAdapter messageListenerAdapter(PaymentListener paymentListener, RedisSerializer<Payment> paymentSerializer) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(paymentListener);
        messageListenerAdapter.setSerializer(paymentSerializer);
        return messageListenerAdapter;
    }
	
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
			ChannelTopic topic, 
			ChannelTopic topicPayment, 
			RedisMessageListener redisMessageListener, 
			MessageListenerAdapter messageListenerAdapter,
			JedisConnectionFactory jedisConnectionFactory ) {
		
	    RedisMessageListenerContainer container = new RedisMessageListenerContainer(); 
	    container.setConnectionFactory(jedisConnectionFactory); 
	    container.setTaskExecutor(Executors.newFixedThreadPool(4));
	    container.addMessageListener(redisMessageListener, Collections.singletonList(topic)); 
	    container.addMessageListener(messageListenerAdapter, topicPayment); 
	    return container; 
	}
	
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
	
	@Bean
	public RedisTemplate<String, Payment> redisTemplatePayment(JedisConnectionFactory jedisConnectionFactory, RedisSerializer<Payment> paymentSerializer) {
	    final RedisTemplate<String, Payment> template = new RedisTemplate<String, Payment>();
	    template.setConnectionFactory(jedisConnectionFactory);
	  	template.setValueSerializer(paymentSerializer);
	    return template;
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
	    final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
	    template.setConnectionFactory(jedisConnectionFactory);
		template.setEnableTransactionSupport(true);
		template.setKeySerializer(RedisSerializer.string());
	  	template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
	    return template;
	}
	
}
