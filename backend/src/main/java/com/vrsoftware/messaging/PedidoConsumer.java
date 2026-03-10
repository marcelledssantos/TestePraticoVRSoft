package com.vrsoftware.messaging;

import com.rabbitmq.client.Channel;
import com.vrsoftware.config.RabbitConfig;
import com.vrsoftware.exception.ExcecaoDeProcessamento;
import com.vrsoftware.model.Pedido;
import com.vrsoftware.model.StatusPedido;
import com.vrsoftware.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class PedidoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoConsumer.class);

    private final PedidoService service;
    private final StatusPedidoProducer statusPedidoProducer;

    public PedidoConsumer(PedidoService service, StatusPedidoProducer statusPedidoProducer) {
        this.service = service;
        this.statusPedidoProducer = statusPedidoProducer;
    }

    @RabbitListener(queues = RabbitConfig.FILA_ENTRADA)
    public void consumir(Pedido pedido,
                         Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException, InterruptedException {
        try {
            log.info("Iniciando processamento do pedido {}", pedido.getId());

            service.atualizarStatus(pedido.getId(), "PROCESSANDO");

            Thread.sleep((long) (1000 + Math.random() * 2000));

            if (Math.random() < 0.2) {
                throw new ExcecaoDeProcessamento("Falha simulada no processamento do pedido");
            }

            StatusPedido statusPedido = new StatusPedido(
                    pedido.getId(),
                    "SUCESSO",
                    null,
                    LocalDateTime.now()
            );

            service.atualizarStatus(pedido.getId(), "SUCESSO");
            statusPedidoProducer.enviarSucesso(statusPedido);

            channel.basicAck(deliveryTag, false);

            log.info("Processamento concluído com sucesso para o pedido {}", pedido.getId());
        } catch (ExcecaoDeProcessamento ex) {
            StatusPedido statusPedido = new StatusPedido(
                    pedido.getId(),
                    "FALHA",
                    ex.getMessage(),
                    LocalDateTime.now()
            );

            service.atualizarStatus(pedido.getId(), "FALHA", ex.getMessage());
            statusPedidoProducer.enviarFalha(statusPedido);

            channel.basicReject(deliveryTag, false);

            log.error("Falha no processamento do pedido {}: {}", pedido.getId(), ex.getMessage());
        }
    }
}