# Documentação de Arquitetura

# Objetivo da Arquitetura

O objetivo principal da arquitetura foi construir um sistema:

- desacoplado
- orientado a eventos
- fácil de manter
- fácil de evoluir
- resiliente
- organizado em responsabilidades claras

---

# Estratégia Arquitetural

A aplicação foi estruturada em camadas.

Separando:

- entrada HTTP
- regras de negócio
- domínio
- persistência
- mensageria

---

# Estrutura dos Pacotes

```text
orders
 ├── api
 ├── application
 ├── domain
 ├── infrastructure
 └── shared
```

---

# Camada API

Responsável por:

- controllers
- DTOs
- validações
- respostas HTTP
- tratamento de erros

Pacotes:

```text
orders.api
```

---

# Camada Application

Responsável pelos casos de uso.

Exemplos:

- criação de pedidos
- processamento assíncrono
- consultas
- publicação de eventos

Pacotes:

```text
orders.application
```

---

# Camada Domain

Responsável pelas regras centrais do sistema.

Contém:

- entidades
- enums
- eventos
- contratos

Pacotes:

```text
orders.domain
```

---

# Camada Infrastructure

Responsável pela comunicação com tecnologias externas.

Exemplos:

- PostgreSQL
- JPA
- Kafka
- repositories

Pacotes:

```text
orders.infrastructure
```

---

# Strategy Pattern

O processamento por tipo de produto foi implementado usando Strategy Pattern.

Foi criado um contrato:

```text
OrderItemProcessor
```

E implementações específicas:

```text
PhysicalOrderItemProcessor
DigitalOrderItemProcessor
SubscriptionOrderItemProcessor
PreOrderItemProcessor
CorporateOrderItemProcessor
```

---

# Motivo da Escolha

Essa abordagem evita:

- if/else gigantes
- alto acoplamento
- crescimento desorganizado

Benefícios:

- melhor manutenção
- maior legibilidade
- extensibilidade
- aderência ao SOLID

---

# Fluxo de Criação do Pedido

```text
Cliente
   ↓
OrderController
   ↓
OrderCreationService
   ↓
ProductCatalogService
   ↓
Banco PostgreSQL
   ↓
Publicação do evento ORDER_CREATED
```

---

# Fluxo Assíncrono

```text
Kafka/Redpanda
   ↓
OrderCreatedConsumer
   ↓
OrderProcessingService
   ↓
Processor específico
   ↓
Atualização do pedido
   ↓
Publicação do resultado
```

---

# Catálogo de Produtos

O sistema possui tabela de produtos populada via Flyway.

Responsabilidades:

- validar existência do produto
- validar disponibilidade
- fornecer preço
- fornecer tipo do produto

---

# Snapshot de Preço

O sistema nunca recebe preço do cliente.

Durante criação do pedido:

- o preço é buscado no catálogo
- salvo no item do pedido
- preservado como snapshot histórico

Isso evita manipulação de preço.

---

# Persistência

Banco utilizado:

```text
PostgreSQL
```

Versionamento:

```text
Flyway
```

---

# Eventos Kafka

Tópico principal:

```text
order-events
```

Eventos:

- ORDER_CREATED
- ORDER_PROCESSED
- ORDER_FAILED
- ORDER_PENDING_APPROVAL

---

# Idempotência

Como Kafka possui semântica at-least-once, mensagens podem ser entregues mais de uma vez.

Para evitar reprocessamento:

- eventos processados são registrados
- eventId é verificado antes do processamento

Tabela:

```text
processed_events
```

---

# Tratamento de Erros

A API utiliza:

```text
@RestControllerAdvice
```

Responsável por:

- padronizar respostas
- mapear exceções
- retornar mensagens amigáveis

---

# Docker

A solução foi containerizada utilizando:

- Dockerfile
- Docker Compose

Serviços:

- app
- postgres
- redpanda

---

# Makefile

Foi criado Makefile para simplificar:

- execução
- build
- logs
- limpeza
- testes

---

# Trade-offs

Algumas simplificações foram feitas devido ao prazo do desafio.

Exemplos:

- antifraude mockado
- pagamento mockado
- logística mockada
- envio de email mockado

O foco foi:

- arquitetura
- mensageria
- organização
- persistência
- processamento assíncrono

---

# Melhorias Futuras

- DLQ
- Retry com backoff
- Outbox Pattern
- Swagger
- Prometheus
- OpenTelemetry
- Segurança
- CI/CD
- Tracing distribuído