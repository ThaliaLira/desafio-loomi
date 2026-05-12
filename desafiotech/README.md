# Sistema de Processamento de Pedidos

Projeto desenvolvido para o desafio técnico da Loomi.

A aplicação implementa um sistema backend orientado a eventos para processamento de pedidos em uma plataforma de e-commerce.

O sistema foi desenvolvido utilizando Java 21, Spring Boot, PostgreSQL e Redpanda/Kafka, com foco em:

- arquitetura limpa
- separação de responsabilidades
- processamento assíncrono
- mensageria
- persistência
- resiliência
- containerização
- documentação

---

# Objetivo do Projeto

O objetivo do sistema é permitir:

- criação de pedidos via API REST
- persistência dos pedidos
- publicação de eventos em Kafka/Redpanda
- processamento assíncrono dos pedidos
- aplicação de regras específicas por tipo de produto
- rastreamento de status
- consulta de pedidos

---

# Tecnologias Utilizadas

## Backend

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Kafka
- Validation API

## Banco de Dados

- PostgreSQL
- Flyway

## Mensageria

- Redpanda (Kafka-compatible)

## Infraestrutura

- Docker
- Docker Compose
- Makefile

## Testes

- JUnit 5
- Testcontainers

---

# Estrutura do Projeto

```text
src
 ├── main
 │    ├── java
 │    │    └── com.loomi.desafiotech
 │    │         └── orders
 │    │              ├── api
 │    │              ├── application
 │    │              ├── domain
 │    │              ├── infrastructure
 │    │              └── shared
 │    │
 │    └── resources
 │         ├── db
 │         └── application-dev.yml
 │
 └── test
```

---

# Arquitetura

O sistema foi construído utilizando arquitetura em camadas, separando:

- API
- aplicação
- domínio
- infraestrutura

O objetivo dessa divisão foi:

- facilitar manutenção
- melhorar legibilidade
- reduzir acoplamento
- facilitar testes
- permitir evolução futura

Detalhes completos estão em:

```text
docs/architecture.md
```

---

# Funcionalidades Implementadas

## API REST

### Criar Pedido

```http
POST /api/orders
```

Responsabilidades:

- validar payload
- buscar produto no catálogo
- calcular preço no backend
- criar snapshot de preço
- persistir pedido
- publicar evento ORDER_CREATED

---

### Consultar Pedido por ID

```http
GET /api/orders/{orderId}
```

---

### Consultar Pedidos por Cliente

```http
GET /api/orders?customerId={customerId}
```

---

# Tipos de Pedido Suportados

O sistema suporta cinco tipos de produto:

| Tipo | Descrição |
|---|---|
| PHYSICAL | Produtos físicos |
| SUBSCRIPTION | Assinaturas |
| DIGITAL | Produtos digitais |
| PRE_ORDER | Pré-vendas |
| CORPORATE | Pedidos corporativos |

Cada tipo possui validações e processamento próprios.

---

# Fluxo Principal do Sistema

```text
Cliente
   ↓
POST /api/orders
   ↓
OrderController
   ↓
OrderCreationService
   ↓
Banco PostgreSQL
   ↓
Evento ORDER_CREATED
   ↓
Redpanda/Kafka
   ↓
Consumer assíncrono
   ↓
Processamento do pedido
   ↓
Atualização do status
   ↓
Publicação do resultado
```

---

# Regras de Negócio Implementadas

## PHYSICAL

- valida estoque
- reduz estoque após processamento
- falha com OUT_OF_STOCK quando necessário
- gera alerta de estoque baixo

---

## DIGITAL

- gera licença
- impede compra duplicada
- registra ownership do produto digital

---

## SUBSCRIPTION

- valida incompatibilidade entre planos
- impede Basic + Enterprise simultaneamente
- valida quantidade de assinaturas

---

## PRE_ORDER

- valida releaseDate
- impede pré-venda de produto já lançado

---

## CORPORATE

- valida presença de CNPJ
- pedidos acima de 50.000 vão para PENDING_APPROVAL

---

# Eventos Implementados

## ORDER_CREATED

Publicado quando um pedido é criado.

---

## ORDER_PROCESSED

Publicado quando um pedido é processado com sucesso.

---

## ORDER_FAILED

Publicado quando ocorre falha no processamento.

---

## ORDER_PENDING_APPROVAL

Publicado quando um pedido corporativo requer aprovação manual.

---

# Banco de Dados

As migrations são controladas via Flyway.

Tabelas principais:

- products
- orders
- order_items
- processed_events
- digital_product_ownership

---

# Idempotência

O sistema implementa idempotência básica para eventos Kafka.

Eventos processados são registrados na tabela:

```text
processed_events
```

Antes de processar um evento, o sistema verifica se ele já foi processado anteriormente.

---

# Como Executar o Projeto

## Pré-requisitos

- Docker Desktop
- Java 21
- Maven
- Git Bash ou terminal equivalente

---

# Executando com Docker

Na raiz do projeto:

```bash
docker compose up --build
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

---

# Executando Localmente

Suba primeiro infraestrutura:

```bash
docker compose up -d postgres redpanda
```

Depois execute a aplicação pelo IntelliJ.

---

# Configuração Local

## PostgreSQL

```text
jdbc:postgresql://localhost:5433/order_db
```

---

## Kafka/Redpanda

```text
localhost:19092
```

---

# Makefile

Comandos disponíveis:

```bash
make up
make down
make logs
make db
make test
make clean
```

---

# Exemplos de Requisição

## Pedido físico

```json
{
  "customerId": "customer-123",
  "items": [
    {
      "productId": "BOOK-CC-001",
      "quantity": 2,
      "metadata": {
        "warehouseLocation": "SP"
      }
    }
  ]
}
```

---

## Pedido digital

```json
{
  "customerId": "customer-digital",
  "items": [
    {
      "productId": "EBOOK-JAVA-001",
      "quantity": 1,
      "metadata": {
        "format": "PDF"
      }
    }
  ]
}
```

---

# Testes

Os testes utilizam:

- JUnit
- Testcontainers
- PostgreSQL Container
- Kafka Container

Fluxo validado:

```text
API → Banco → Evento → Consumer → Atualização de Status
```

---

# Documentações Complementares

## Arquitetura

```text
docs/architecture.md
```

## Uso de IA

```text
docs/ai-usage.md
```

## Relatório de progresso

```text
docs/progress-report.md
```

---

# Melhorias Futuras

Com mais tempo eu implementaria:

- DLQ
- Retry com exponential backoff
- Swagger/OpenAPI
- Observabilidade com Prometheus
- Tracing distribuído
- Outbox Pattern
- Autenticação
- CI/CD
- Métricas

---

# Conclusão

A solução foi construída priorizando:

- fluxo funcional completo
- clareza arquitetural
- separação de responsabilidades
- documentação
- facilidade de execução local
- aderência ao escopo do desafio