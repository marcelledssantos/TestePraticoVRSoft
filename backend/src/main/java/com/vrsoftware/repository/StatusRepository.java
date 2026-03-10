package com.vrsoftware.repository;

import com.vrsoftware.model.StatusPedido;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StatusRepository {

    private final Map<UUID, StatusPedido> statusMap = new ConcurrentHashMap<>();

    public void salvar(UUID id, String status) {
        statusMap.put(id, new StatusPedido(id, status, null, LocalDateTime.now()));
    }

    public void salvar(UUID id, String status, String mensagemErro) {
        statusMap.put(id, new StatusPedido(id, status, mensagemErro, LocalDateTime.now()));
    }

    public StatusPedido buscar(UUID id) {
        return statusMap.get(id);
    }
}