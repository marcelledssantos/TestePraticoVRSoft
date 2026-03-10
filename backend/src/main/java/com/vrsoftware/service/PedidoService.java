package com.vrsoftware.service;

import com.vrsoftware.messaging.PedidoProducer;
import com.vrsoftware.model.Pedido;
import com.vrsoftware.model.StatusPedido;
import com.vrsoftware.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoProducer producer;
    private final StatusRepository repository;

    public PedidoService(PedidoProducer producer, StatusRepository repository) {
        this.producer = producer;
        this.repository = repository;
    }

    public UUID criarPedido(Pedido pedido) {
        repository.salvar(pedido.getId(), "RECEBIDO");
        producer.enviar(pedido);
        return pedido.getId();
    }

    public StatusPedido buscarStatus(UUID id) {
        return repository.buscar(id);
    }

    public void atualizarStatus(UUID id, String status) {
        repository.salvar(id, status);
    }

    public void atualizarStatus(UUID id, String status, String mensagemErro) {
        repository.salvar(id, status, mensagemErro);
    }
}