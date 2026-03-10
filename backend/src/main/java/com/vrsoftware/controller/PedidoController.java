package com.vrsoftware.controller;

import com.vrsoftware.model.Pedido;
import com.vrsoftware.model.StatusPedido;
import com.vrsoftware.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UUID> criar(@Valid @RequestBody Pedido pedido) {
        UUID id = service.criarPedido(pedido);
        return ResponseEntity.accepted().body(id);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<StatusPedido> status(@PathVariable UUID id) {
        StatusPedido status = service.buscarStatus(id);

        if (status == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(status);
    }
}