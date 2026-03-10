package com.vrsoftware.messaging;

import com.vrsoftware.config.RabbitConfig;
import com.vrsoftware.model.StatusPedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class StatusPedidoProducer {

    private final RabbitTemplate rabbitTemplate;

    public StatusPedidoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarSucesso(StatusPedido statusPedido) {
        rabbitTemplate.convertAndSend(RabbitConfig.FILA_SUCESSO, statusPedido);
    }

    public void enviarFalha(StatusPedido statusPedido) {
        rabbitTemplate.convertAndSend(RabbitConfig.FILA_FALHA, statusPedido);
    }
}