package com.cjg.traveling.common.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.cjg.traveling.dto.OpinionDto;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class KafkaProducer {
	
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
		
	public void create(String topic, OpinionDto message) {
		logger.info("topic : {}, message : {}", topic, message);
		kafkaTemplate.send(topic, message);
	}

}
