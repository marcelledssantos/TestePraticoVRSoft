# 🚀 TestePraticoVRSoft

Sistema de pedidos assíncrono composto por um backend Spring Boot e um cliente desktop Java Swing, utilizando RabbitMQ para processamento assíncrono de mensagens.

---

## ⚙️ Tecnologias utilizadas

- Java 17+
- Spring Boot
- RabbitMQ
- Java Swing
- Maven
- Jackson
- JUnit 5
- Mockito

---

## 🧠 Visão geral da solução

O projeto simula um fluxo completo de processamento assíncrono de pedidos, desacoplando a entrada via API do processamento através de mensageria.

A arquitetura foi dividida em dois módulos principais:

- backend: responsável por expor a API REST, publicar pedidos no RabbitMQ, consumir e processar mensagens de forma assíncrona, manter o status em memória e disponibilizar endpoint de consulta.
- swing-client: responsável por fornecer a interface gráfica desktop para envio de pedidos e acompanhamento do status via polling.

---

## 🏗️ Estrutura do projeto

TestePraticoVRSoft/
├── backend/
├── swing-client/
├── README.md
└── .gitignore

---

## 📦 Requisitos

- Java 17 ou superior
- Maven 3.8+
- Git
- Docker ou Podman

---

## 🐰 Subindo o RabbitMQ

podman run -d \
  --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  docker.io/library/rabbitmq:3-management

Painel:
http://localhost:15672

Credenciais:
guest / guest

---

## 📬 Filas utilizadas

- pedidos.entrada.marcelle.v2
- pedidos.entrada.marcelle.v2.dlq
- pedidos.status.sucesso.marcelle.v2
- pedidos.status.falha.marcelle.v2

---

## 🚀 Backend

cd backend
mvn spring-boot:run

http://localhost:8080

---

## 🔌 Endpoint

POST /api/pedidos

{
  "produto": "Mouse",
  "quantidade": 2
}

GET /api/pedidos/status/{id}

---

## 🖥️ Swing Client

cd swing-client
mvn clean compile
mvn exec:java -Dexec.mainClass="com.vrsoftware.client.SwingApplication"

---

## 🔄 Fluxo

1. Swing envia pedido
2. Backend recebe
3. Vai para RabbitMQ
4. Consumer processa
5. Atualiza status
6. Swing consulta

---

## 📊 Status

- RECEBIDO
- PROCESSANDO
- SUCESSO
- FALHA

---

## ⚠️ Correções

- Tratamento de erro (500)
- Serialização JSON
- Ack manual no RabbitMQ
- DLQ configurada

---

## 🧩 Boas práticas

- Arquitetura em camadas
- Producer e Consumer
- Processamento assíncrono
- Tratamento de exceções
- Testes com Mockito
