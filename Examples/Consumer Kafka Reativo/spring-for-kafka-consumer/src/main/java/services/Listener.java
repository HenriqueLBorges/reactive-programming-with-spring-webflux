package services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Listener {

    //@KafkaListener(topics = "${topic.name}", containerFactory = "ConcurrentKafkaListenerContainerFactory")
    @KafkaListener(topics = "KAFKA_TOPIC")
    public void listen(String value) {
        System.out.println("value = " + value);

        // ack.acknowledge();
    }

}
