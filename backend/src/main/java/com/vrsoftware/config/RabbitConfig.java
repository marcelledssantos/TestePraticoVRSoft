package com.vrsoftware.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}