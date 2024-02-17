package com.cjg.traveling.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class KafkaConsumer {
		
	@KafkaListener(topics="opinion")
	public void listener(Object data) {
		ConsumerRecord consumerRecord = (ConsumerRecord)data;
		String value = (String)consumerRecord.value();
		
		JsonObject jo = JsonParser.parseString(value).getAsJsonObject();
		String id = jo.get("opinionId").getAsString();
		String userId = jo.get("userId").getAsString();
		
		System.out.println("id : " + id);
		System.out.println("userId : " + userId);
		
		System.out.println(value);
		
		
	}

}
