# TestePraticoVRSoft

Sistema de pedidos assíncrono composto por um backend Spring Boot e um cliente desktop Java Swing, utilizando RabbitMQ para processamento assíncrono de mensagens.

## Tecnologias utilizadas

* Java 17
* Spring Boot
* RabbitMQ
* Java Swing
* Maven
* Jackson
* JUnit 5
* Mockito

## Visão geral da solução

O projeto foi dividido em dois módulos principais:

* `backend`: responsável por expor a API REST, publicar pedidos no RabbitMQ, consumir e processar mensagens de forma assíncrona, manter o status em memória e disponibilizar endpoint de consulta.
* `swing-client`: responsável por fornecer a interface gráfica desktop para envio de pedidos e acompanhamento do status via polling.

## Estrutura do projeto

```text
TestePraticoVRSoft/
├── backend/
├── swing-client/
├── README.md
└── .gitignore
```

## Requisitos

Antes de executar o projeto, é necessário ter instalado:

* Java 17
* Maven 3.8+
* Git

## Configuração do RabbitMQ

Arquivo:

`backend/src/main/resources/application.yml`

## Filas utilizadas

* `pedidos.entrada.marcelle.v2`
* `pedidos.entrada.marcelle.v2.dlq`
* `pedidos.status.sucesso.marcelle.v2`
* `pedidos.status.falha.marcelle.v2`

# Backend

## Como executar o backend

```bash
cd backend
mvn spring-boot:run
```

Backend disponível em:

`http://localhost:8080`

## Endpoint principal

### POST /api/pedidos

```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "produto": "Mouse",
  "quantidade": 2,
  "dataCriacao": "2026-03-10T20:30:00"
}
```

### GET /api/pedidos/status/{id}

# Testes backend

```bash
cd backend
mvn test
```

# Swing Client

## Como executar o Swing

```bash
cd swing-client
mvn clean compile
mvn exec:java
```

Ou executar a classe:

`com.vrsoftware.client.SwingApplication`

## Fluxo esperado

1. Informar produto e quantidade
2. Clicar em enviar pedido
3. Acompanhar atualização automática do status

## Status possíveis

* RECEBIDO
* PROCESSANDO
* SUCESSO
* FALHA

# Fluxo completo

1. Swing envia pedido
2. Backend recebe via HTTP
3. Pedido vai para RabbitMQ
4. Consumer processa
5. Status atualizado em memória
6. Swing consulta status periodicamente

# Boas práticas aplicadas

* Separação em camadas
* Producer e consumer dedicados
* DLQ configurada
* Polling assíncrono no Swing
* Atualização de interface na EDT
* Teste unitário da publicação em fila
