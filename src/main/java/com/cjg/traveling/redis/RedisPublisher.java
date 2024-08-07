package com.cjg.traveling.redis;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
	
	Logger logger = LoggerFactory.getLogger(RedisPublisher.class);
	
	private final ChannelTopic channelTopic;
	private final RedisTemplate<String, Object> redisTemplate;
	
	public void sendMessage(String message) {
		redisTemplate.convertAndSend(channelTopic.getTopic(), message);
	}	

}
