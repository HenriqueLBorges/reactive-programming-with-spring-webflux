package com.example.springkafkaconsumer.services;

import com.example.springkafkaconsumer.clients.BingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class KafkaConsumerListener {
    final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);
    final private BingClient client;

    public KafkaConsumerListener(BingClient client) {
        this.client = client;
    }

    @KafkaListener(topics = "${topic.name}", containerFactory = "ConcurrentKafkaListenerContainerFactory")
    public void listen(String value/*, Acknowledgment acknowledgment*/) {
        /*acknowledgment.acknowledge();*/
        logger.info("Record arrived: " + value);
        logger.info("Response arrived: " + client.searchWord(value));
    }
}
