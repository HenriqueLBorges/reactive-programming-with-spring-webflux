# Consumer Kafka Reativo

## Sumário
1. [Objetivo](##Objetivo)
2. [Resultados](##Resultados)

## Objetivo

O objetivo desse exemplo é comparar um <i>Consumer</i> Kafka construído na linguagem Java e [<i>Spring for Apache Kafka</i>](https://spring.io/projects/spring-kafka) com um outro <i>Consumer</i> Kafka contruído na linguagem Java com [<i>Spring Webflux</i>](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux) e [<i>reactor-kafka</i>](https://projectreactor.io/docs/kafka/release/reference/).

<div style="text-align:center"><img src="../../misc/images/figure-13.png" width="800"/></div>

Para realizarmos a comparação, foi criada uma [lista de nomes de animais](Examples/Consumer%20Kafka%20Reativo/kafka-producer/src/main/resources/animals.txt). O projeto do [kafka-producer](Examples/Consumer%20Kafka%20Reativo/kafka-producer) lê essa lista e insere os nomes dentro da plataforma Kafka. Uma vez inseridos, os nomes podem ser consumidos de maneira concorrente pelo [spring-for-kafka-consumer](Examples/Consumer%20Kafka%20Reativo/spring-for-kafka-consumer) e [spring-reactive-kafka-consumer](Examples/Consumer%20Kafka%20Reativo/spring-reactive-kafka-consumer). Os <i>consumers</i> por sua vez iniciam uma requisição HTTP com a [<i>API do Bing</i>](https://azure.microsoft.com/pt-br/pricing/details/cognitive-services/search-api/) para cada nome recebido.

## Resultados