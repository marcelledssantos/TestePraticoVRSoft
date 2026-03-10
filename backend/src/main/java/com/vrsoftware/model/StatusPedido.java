package com.vrsoftware.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class StatusPedido {

    private UUID idPedido;
    private String status;
    private String mensagemErro;
    private LocalDateTime dataProcessamento;

    public StatusPedido() {
    }

    public StatusPedido(UUID idPedido, String status, String mensagemErro, LocalDateTime dataProcessamento) {
        this.idPedido = idPedido;
        this.status = status;
        this.mensagemErro = mensagemErro;
        this.dataProcessamento = dataProcessamento;
    }

    public UUID getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(UUID idPedido) {
        this.idPedido = idPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public LocalDateTime getDataProcessamento() {
        return dataProcessamento;
    }

    public void setDataProcessamento(LocalDateTime dataProcessamento) {
        this.dataProcessamento = dataProcessamento;
    }
}