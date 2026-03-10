package com.vrsoftware.messaging;

import com.vrsoftware.config.RabbitConfig;
import com.vrsoftware.model.Pedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PedidoProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PedidoProducer pedidoProducer;

    @Test
    void devePublicarPedidoNaFilaDeEntrada() {
        Pedido pedido = new Pedido(
                UUID.randomUUID(),
                "Mouse",
                2,
                LocalDateTime.now()
        );

        pedidoProducer.enviar(pedido);

        verify(rabbitTemplate).convertAndSend(RabbitConfig.FILA_ENTRADA, pedido);
    }
}