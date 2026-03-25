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

    @RabbitListener(
            queues = RabbitConfig.FILA_ENTRADA,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consumir(Pedido pedido,
                         Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("Iniciando processamento do pedido {}", pedido.getId());

            service.atualizarStatus(pedido.getId(), "PROCESSANDO");

            simularProcessamento();

            if (houveFalhaSimulada()) {
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
            tratarFalhaDeNegocio(pedido, ex);
            channel.basicReject(deliveryTag, false);
        } catch (Exception ex) {
            tratarFalhaInesperada(pedido, ex);
            channel.basicReject(deliveryTag, false);
        }
    }

    private void simularProcessamento() throws InterruptedException {
        Thread.sleep((long) (1000 + Math.random() * 2000));
    }

    private boolean houveFalhaSimulada() {
        return Math.random() < 0.2;
    }

    private void tratarFalhaDeNegocio(Pedido pedido, ExcecaoDeProcessamento ex) {
        StatusPedido statusPedido = new StatusPedido(
                pedido.getId(),
                "FALHA",
                ex.getMessage(),
                LocalDateTime.now()
        );

        service.atualizarStatus(pedido.getId(), "FALHA", ex.getMessage());
        statusPedidoProducer.enviarFalha(statusPedido);

        log.error("Falha de processamento do pedido {}: {}", pedido.getId(), ex.getMessage());
    }

    private void tratarFalhaInesperada(Pedido pedido, Exception ex) {
        String mensagemErro = "Erro inesperado durante o processamento";

        StatusPedido statusPedido = new StatusPedido(
                pedido.getId(),
                "FALHA",
                mensagemErro,
                LocalDateTime.now()
        );

        service.atualizarStatus(pedido.getId(), "FALHA", mensagemErro);
        statusPedidoProducer.enviarFalha(statusPedido);

        log.error("Erro inesperado ao processar pedido {}", pedido.getId(), ex);
    }
}