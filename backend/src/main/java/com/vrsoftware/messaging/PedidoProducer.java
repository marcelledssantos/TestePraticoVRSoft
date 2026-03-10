package com.vrsoftware.messaging;

import com.vrsoftware.config.RabbitConfig;
import com.vrsoftware.model.Pedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PedidoProducer {

    private final RabbitTemplate rabbitTemplate;

    public PedidoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviar(Pedido pedido) {
        rabbitTemplate.convertAndSend(RabbitConfig.FILA_ENTRADA, pedido);
    }
}