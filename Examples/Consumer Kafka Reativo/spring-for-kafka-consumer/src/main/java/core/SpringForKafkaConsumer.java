package core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
// import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
// @EnableKafka
// @EnableFeignClients
public class SpringForKafkaConsumer {

    public static void main(String[] args){
        SpringApplication.run(SpringForKafkaConsumer.class, args);
    }

}
