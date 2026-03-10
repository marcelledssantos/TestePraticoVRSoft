package com.vrsoftware.client.ui;

import com.vrsoftware.client.model.Pedido;
import com.vrsoftware.client.model.StatusPedido;
import com.vrsoftware.client.service.ApiClient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class MainFrame extends JFrame {

    private final JTextField produtoField = new JTextField(15);
    private final JTextField quantidadeField = new JTextField(5);
    private final JTextArea areaStatus = new JTextArea();
    private final ApiClient apiClient = new ApiClient();
    private final Map<UUID, String> pedidosPendentes = new LinkedHashMap<>();

    public MainFrame() {
        setTitle("Sistema de Pedidos");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        areaStatus.setEditable(false);

        JButton enviarButton = new JButton("Enviar Pedido");
        enviarButton.addActionListener(e -> enviarPedido());

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Produto:"));
        formPanel.add(produtoField);
        formPanel.add(new JLabel("Quantidade:"));
        formPanel.add(quantidadeField);
        formPanel.add(enviarButton);

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(areaStatus), BorderLayout.CENTER);

        iniciarPolling();

        setVisible(true);
    }

    private void enviarPedido() {
        String produto = produtoField.getText().trim();
        String quantidadeTexto = quantidadeField.getText().trim();

        if (produto.isBlank()) {
            JOptionPane.showMessageDialog(this, "Informe o produto.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeTexto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            return;
        }

        if (quantidade <= 0) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
            return;
        }

        Pedido pedido = new Pedido(
                UUID.randomUUID(),
                produto,
                quantidade,
                LocalDateTime.now()
        );

        try {
            UUID id = apiClient.enviarPedido(pedido);
            pedidosPendentes.put(id, "ENVIADO, AGUARDANDO PROCESSO");
            atualizarAreaStatus();
            produtoField.setText("");
            quantidadeField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao enviar pedido: " + e.getMessage());
        }
    }

    private void iniciarPolling() {
        Timer timer = new Timer(3000, e -> consultarPedidosPendentes());
        timer.start();
    }

    private void consultarPedidosPendentes() {
        Map<UUID, String> atualizados = new LinkedHashMap<>(pedidosPendentes);

        for (UUID id : pedidosPendentes.keySet()) {
            try {
                StatusPedido statusPedido = apiClient.buscarStatus(id);

                if (statusPedido == null) {
                    continue;
                }

                String status = statusPedido.getStatus();

                if ("SUCESSO".equals(status) || "FALHA".equals(status) || "PROCESSANDO".equals(status) || "RECEBIDO".equals(status)) {
                    atualizados.put(id, montarTextoStatus(statusPedido));
                }
            } catch (Exception ex) {
                atualizados.put(id, "ERRO AO CONSULTAR STATUS");
            }
        }

        SwingUtilities.invokeLater(() -> {
            pedidosPendentes.clear();
            pedidosPendentes.putAll(atualizados);
            atualizarAreaStatus();
        });
    }

    private String montarTextoStatus(StatusPedido statusPedido) {
        if ("FALHA".equals(statusPedido.getStatus()) && statusPedido.getMensagemErro() != null) {
            return "FALHA - " + statusPedido.getMensagemErro();
        }
        return statusPedido.getStatus();
    }

    private void atualizarAreaStatus() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<UUID, String> entry : pedidosPendentes.entrySet()) {
            builder.append(entry.getKey())
                    .append(" - ")
                    .append(entry.getValue())
                    .append("\n");
        }

        areaStatus.setText(builder.toString());
    }
}