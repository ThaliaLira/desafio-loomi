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
- testes automatizados

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
- controle de ownership digital
- processamento corporativo com aprovação manual
- validação de pré-venda
- validação de assinatura

---

# Tecnologias Utilizadas

## Backend

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Kafka
- Validation API
- Jackson

---

## Banco de Dados

- PostgreSQL
- Flyway

---

## Mensageria

- Redpanda (Kafka-compatible)

---

## Infraestrutura

- Docker
- Docker Compose
- Makefile

---

## Testes

- JUnit 5
- Mockito
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
      ├── java
      └── resources

docs
 ├── architecture.md
 ├── ai-usage.md
 ├── progress-report.md
 └── postman
      └── loomi-order-processing.postman_collection.json
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

Para executar o projeto é necessário possuir instalado:

- Java 21
- Maven
- Docker Desktop
- Git

---

# Executando com Docker Compose

Na raiz do projeto execute:

```bash
docker compose up --build
```

Esse comando irá subir:

- aplicação Spring Boot
- PostgreSQL
- Redpanda/Kafka

---

# Endereço da Aplicação

Quando executada via Docker Compose, a API ficará disponível em:

```text
http://localhost:8080
```

---

# Executando Localmente pelo IntelliJ

Caso deseje executar a aplicação diretamente pelo IntelliJ:

## 1. Suba apenas infraestrutura

```bash
docker compose up -d postgres redpanda
```

---

## 2. Execute a classe principal

```text
DesafiotechApplication
```

---

# Porta Local Alternativa

Durante o desenvolvimento foi identificado conflito local na porta `8080`.

Por esse motivo, em algumas execuções locais via IntelliJ a aplicação pode utilizar a porta:

```text
http://localhost:8081
```

Essa alteração foi utilizada apenas para evitar conflito local de ambiente.

A execução oficial via Docker Compose permanece utilizando:

```text
http://localhost:8080
```

---

# Testando a API

## Criar Pedido

Via Docker Compose:

```text
POST http://localhost:8080/api/orders
```

Via IntelliJ/local:

```text
POST http://localhost:8081/api/orders
```

---

# Exemplos de Payload

## Pedido Físico

```json
{
  "customerId": "customer-physical-001",
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

## Pedido Digital

```json
{
  "customerId": "customer-digital-001",
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

## Pedido de Assinatura

```json
{
  "customerId": "customer-sub-001",
  "items": [
    {
      "productId": "SUB-PREMIUM-001",
      "quantity": 1,
      "metadata": {
        "billingCycle": "MONTHLY",
        "autoRenewal": true
      }
    }
  ]
}
```

---

## Pedido Corporativo

```json
{
  "customerId": "company-acme",
  "items": [
    {
      "productId": "CORP-LICENSE-ENT",
      "quantity": 5,
      "metadata": {
        "cnpj": "12.345.678/0001-90",
        "paymentTerms": "NET_60"
      }
    }
  ]
}
```

---

# Banco de Dados

## PostgreSQL

A aplicação utiliza PostgreSQL como banco relacional principal.

Conexão local:

```text
jdbc:postgresql://localhost:5433/order_db
```

Usuário:

```text
postgres
```

Senha:

```text
postgres
```

---

# Kafka/Redpanda

O sistema utiliza Redpanda como broker Kafka-compatible.

Broker local:

```text
localhost:19092
```

Topic principal:

```text
order-events
```

---

# Comandos Úteis

## Subir aplicação

```bash
docker compose up --build
```

---

## Derrubar containers

```bash
docker compose down
```

---

## Ver logs

```bash
docker compose logs -f
```

---

## Rodar testes

```bash
./mvnw.cmd test
```

---

# Build da Aplicação

```bash
./mvnw.cmd clean package
```

---

# Testes

Os testes utilizam:

- JUnit
- Mockito
- Testcontainers
- PostgreSQL Container
- Kafka Container

Fluxo validado:

```text
API → Banco → Evento → Consumer → Atualização de Status
```

---

# Tipos de Testes Implementados

- unitários
- integração
- fluxo de processamento
- validação de regras de negócio

---

# Postman Collection

O projeto inclui uma collection Postman contendo cenários completos da API.

Arquivo:

```text
docs/postman/loomi-order-processing.postman_collection.json
```

A collection foi criada para facilitar:

- validação manual da API
- testes rápidos do fluxo principal
- demonstração dos cenários de negócio
- avaliação técnica do desafio

---

# Como Importar a Collection

1. Abra o Postman
2. Clique em:

```text
Import
```

3. Selecione:

```text
docs/postman/loomi-order-processing.postman_collection.json
```

4. A collection será carregada automaticamente

---

# Variável de Ambiente

A collection utiliza:

```text
{{baseUrl}}
```

Valor padrão:

```text
http://localhost:8080
```

Caso esteja executando localmente via IntelliJ:

```text
http://localhost:8081
```

---

# Cenários Disponíveis na Collection

## Pedido Físico com Sucesso

Valida:

- criação do pedido
- decremento de estoque
- persistência correta

---

## Pedido Digital com Sucesso

Valida:

- ownership digital
- persistência da compra

---

## Produto Digital Já Adquirido

Valida:

- bloqueio de recompra duplicada

---

## Assinatura com Sucesso

Valida:

- criação de assinatura válida

---

## Assinaturas Incompatíveis

Valida:

- bloqueio Basic + Enterprise

---

## Pré-venda com Data Futura

Valida:

- releaseDate válida

---

## Pré-venda com Data Inválida

Valida:

- bloqueio de releaseDate passada

---

## Pedido Corporativo

Valida:

- presença obrigatória de CNPJ
- aprovação manual para alto valor

---

## Produto Inexistente

Valida:

- tratamento de produto não encontrado

---

## Payload Inválido

Valida:

- Bean Validation
- tratamento de erro de request

---

## Consulta por ID

Endpoint:

```text
GET /api/orders/{orderId}
```

---

## Consulta por Cliente

Endpoint:

```text
GET /api/orders?customerId=...
```

---

# Documentações Complementares

## Arquitetura

```text
docs/architecture.md
```

---

## Uso de IA

```text
docs/ai-usage.md
```

---

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
- Rate limiting

---

# Considerações de Desenvolvimento

Durante o desenvolvimento foram enfrentados desafios relacionados principalmente a:

- setup de ambiente
- Docker Desktop
- configuração do Redpanda/Kafka
- conflitos de porta
- comunicação entre containers
- troubleshooting de dependências Maven

As soluções adotadas priorizaram:

- estabilidade da aplicação
- evolução contínua
- clareza arquitetural
- consistência técnica

---

# Estratégia de Desenvolvimento

Durante o desenvolvimento inicial do projeto, os commits foram realizados diretamente na branch `main` enquanto o ambiente, a infraestrutura e a base da aplicação estavam sendo estabilizados.

Devido ao prazo reduzido do desafio e ao tempo investido na resolução de problemas de setup e configuração, a prioridade foi manter a evolução contínua da aplicação e a entrega funcional do fluxo principal do sistema.

Apesar disso, houve preocupação constante com:

- organização lógica das funcionalidades
- conventional commits
- separação clara de responsabilidades
- documentação
- consistência arquitetural

---

# Conclusão

A solução foi construída priorizando:

- fluxo funcional completo
- clareza arquitetural
- separação de responsabilidades
- documentação
- facilidade de execução local
- aderência ao escopo do desafio

---

# Autor

Thalia Lira
