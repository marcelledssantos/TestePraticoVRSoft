package com.vrsoftware.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String FILA_ENTRADA = "pedidos.entrada.marcelle.v2";
    public static final String FILA_ENTRADA_DLQ = "pedidos.entrada.marcelle.v2.dlq";
    public static final String FILA_SUCESSO = "pedidos.status.sucesso.marcelle.v2";
    public static final String FILA_FALHA = "pedidos.status.falha.marcelle.v2";

    @Bean
    public Queue filaEntrada() {
        return QueueBuilder.durable(FILA_ENTRADA)
                .deadLetterExchange("")
                .deadLetterRoutingKey(FILA_ENTRADA_DLQ)
                .build();
    }

    @Bean
    public Queue filaEntradaDlq() {
        return QueueBuilder.durable(FILA_ENTRADA_DLQ).build();
    }

    @Bean
    public Queue filaSucesso() {
        return QueueBuilder.durable(FILA_SUCESSO).build();
    }

    @Bean
    public Queue filaFalha() {
        return QueueBuilder.durable(FILA_FALHA).build();
    }
}