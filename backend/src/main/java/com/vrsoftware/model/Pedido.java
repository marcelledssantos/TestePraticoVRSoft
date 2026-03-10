package com.vrsoftware.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pedido {

    @NotNull
    private UUID id;

    @NotBlank
    private String produto;

    @Min(1)
    private int quantidade;

    @NotNull
    private LocalDateTime dataCriacao;

    public Pedido() {
    }

    public Pedido(UUID id, String produto, int quantidade, LocalDateTime dataCriacao) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.dataCriacao = dataCriacao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}