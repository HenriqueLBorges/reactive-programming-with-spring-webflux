package com.example.springreactivekafkaconsumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;
import java.util.Collections;

@Configuration
public class KafkaConfiguration {

    @Bean
    public ReceiverOptions<Long, String> kafkaReceiverOptions(@Value(value = "${topic.name}") String topic, KafkaProperties kafkaProperties) {
        ReceiverOptions<Long, String> basicReceiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<Long, String> reactiveKafkaConsumerTemplate(ReceiverOptions<Long, String> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<Long, String>(kafkaReceiverOptions);
    }
}
