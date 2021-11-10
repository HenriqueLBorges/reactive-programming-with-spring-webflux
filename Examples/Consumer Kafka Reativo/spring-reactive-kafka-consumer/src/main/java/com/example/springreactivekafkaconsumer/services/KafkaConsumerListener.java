package com.example.springreactivekafkaconsumer.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class KafkaConsumerListener implements CommandLineRunner {
    final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);
    final private ReactiveKafkaConsumerTemplate<Long, String> reactiveKafkaConsumerTemplate;
    final private WebClient webClient = WebClient.create("https://www.google.com");
    
    public KafkaConsumerListener(ReactiveKafkaConsumerTemplate<Long, String> reactiveKafkaConsumerTemplate) {
        this.reactiveKafkaConsumerTemplate = reactiveKafkaConsumerTemplate;
    }

    /**
     * Cria uma requisição HTTP para a API do ping, pesquisando a palavra recebida no argumento.
     *
     * @param record - Evento consumido do Kafka
     * @return String - Valor extraído do evento Kafka
     */
    private String processMessage(ConsumerRecord<Long, String> record) {
        return record.value();
    }

    /**
     * Cria uma requisição HTTP para a API do ping, pesquisando a palavra recebida no argumento.
     *
     * @param word - Palavra que será utilizada na pesquisa
     * @return Mono<String> - Uma stream reativa que emite 0 ou 1 elementos (resposta da requisição HTTP realizada para a API do bing)
     */
    private Mono<String> requestBing(String word) {
        return webClient.get() //WebClient has its own netty thread pool
                .uri("/search?hl=en&q= {word}", word)
                .retrieve()// Inicia a requisição
                .bodyToMono(String.class)// Converte o objeto para um Mono String (dado que temos a possibilidade de 0 ou 1 resultado)
                .doOnNext(f -> logger.info("Response arrived: " + f))// Printa a resposta
                .doOnError(throwable -> logger.error("something bad happened while requesting : {}", throwable.getMessage())) //Printa em caso de erro
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10)).filter(this::is4xxServerError)); // Seta uma política de retry de no máximo 3 tentativas a cada 10 segundos quando o erro é 4xx
    }

    /**
     * Verifica se o erro HTTP é um erro 4XX
     *
     * @param throwable - erro ocorrido
     * @return boolean - Resposta da verificação
     */
    private boolean is4xxServerError(Throwable throwable) {
        return throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().is4xxClientError();
    }

    /**
     * Consome e extrai o conteúdo de cada evento Kafka
     *
     * @param args - argumentos
     * @return Flux<String> - Uma stream reativa que emite 0 ou N elementos (conteúdo de eventos Kafka)
     */
    private Flux<String> consumeKafkaTopic() {
        return reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                //.delayElements(Duration.ofMillis(10)) // nos permite atrasar emissões de eventos
                .map(this::processMessage)// extrai o conteúdo de cada evento
                .doOnNext(f -> logger.info("Record arrived: " + f)) // Printa cada conteúdo recebido na stream
                .doOnError(throwable -> logger.error("something bad happened while consuming : {}", throwable.getMessage())); //Printa em caso de erro
    }

    /**
     * Inicia o Kafka consumer
     *
     * @param args - argumentos
     */
    @Override
    public void run(String... args) {
        // Utiliza o scheduler boudedElastic para processar
        this.consumeKafkaTopic().subscribe(s -> this.requestBing(s).subscribe());
    }
}
