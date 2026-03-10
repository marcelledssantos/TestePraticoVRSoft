package com.vrsoftware.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vrsoftware.client.model.Pedido;
import com.vrsoftware.client.model.StatusPedido;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/pedidos";
    private final ObjectMapper objectMapper;

    public ApiClient() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public UUID enviarPedido(Pedido pedido) throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            objectMapper.writeValue(outputStream, pedido);
        }

        int statusCode = connection.getResponseCode();

        if (statusCode != 202) {
            throw new IOException("Erro ao enviar pedido. HTTP: " + statusCode);
        }

        try (InputStream inputStream = connection.getInputStream()) {
            return objectMapper.readValue(inputStream, UUID.class);
        }
    }

    public StatusPedido buscarStatus(UUID id) throws IOException {
        URL url = new URL(BASE_URL + "/status/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        int statusCode = connection.getResponseCode();

        if (statusCode == 404) {
            return null;
        }

        if (statusCode != 200) {
            throw new IOException("Erro ao buscar status. HTTP: " + statusCode);
        }

        try (InputStream inputStream = connection.getInputStream()) {
            return objectMapper.readValue(inputStream, StatusPedido.class);
        }
    }
}