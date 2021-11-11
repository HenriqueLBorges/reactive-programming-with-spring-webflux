package com.example.springkafkaconsumer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
public class KafkaConfiguration {

    @Autowired
    KafkaProperties properties;

    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties());
    }

    @Bean(name = "ConcurrentKafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Long, String>>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<Long, String> listener =
                new ConcurrentKafkaListenerContainerFactory<>();

        listener.setConsumerFactory(consumerFactory());
        listener.getContainerProperties()
                .setMissingTopicsFatal(false);
        // listener.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return listener;
    }
}
