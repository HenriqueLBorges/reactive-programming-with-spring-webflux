package com.example.springreactivekafkaconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringReactiveKafkaConsumerApplication {

	/**
	 * Método principal da aplicação Spring
	 *
	 * @param args - Argumentos da aplicação
	 * @return String - Valor extraído do evento Kafka
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveKafkaConsumerApplication.class, args);
	}

}
