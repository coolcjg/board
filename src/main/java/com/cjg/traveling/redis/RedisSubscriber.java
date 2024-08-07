package com.cjg.traveling.redis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
	
	Logger logger = LoggerFactory.getLogger(RedisSubscriber.class);
	private final RedisTemplate<String, Object> redisTemplate;
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
		logger.info("redis sub message {}", publishMessage);
	}

}
