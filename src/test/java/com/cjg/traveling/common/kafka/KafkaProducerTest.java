package com.cjg.traveling.common.kafka;


import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerTest {

    @InjectMocks
    KafkaProducer KafkaProducer;

    @Mock
    KafkaTemplate kafkaTemplate;

    @Test
    @DisplayName("producer 성공")
    public void create(){
        String topic = "topic";
        String message = "message";

        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("topic", "1", "1");

        TopicPartition tp = new TopicPartition("topic", 1);
        RecordMetadata rm = new RecordMetadata(tp, 1,1,1,1,1);

        SendResult<String,Object> sendResult = new SendResult<>(producerRecord, rm);

        CompletableFuture<SendResult<String,Object>> future = CompletableFuture.completedFuture(sendResult);

        given(kafkaTemplate.send(topic, message)).willReturn(future);

        KafkaProducer.create(topic, message);
    }

    @Test
    @DisplayName("producer 실패")
    public void create_2(){
        String topic = "topic";
        String message = "message";

        CompletableFuture<SendResult<String,Object>> future = CompletableFuture.failedFuture(new Exception("실패"));

        given(kafkaTemplate.send(topic, message)).willReturn(future);

        KafkaProducer.create(topic, message);
    }
}
