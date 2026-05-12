# Relatório de Progresso

# Organização das Atividades

O desenvolvimento foi organizado em backlog incremental.

O objetivo foi reduzir riscos e garantir um fluxo funcional completo antes das melhorias secundárias.

---

# Estratégia de Priorização

A ordem de implementação foi:

1. setup da aplicação
2. banco de dados
3. modelagem do domínio
4. API REST
5. catálogo de produtos
6. persistência
7. Kafka/Redpanda
8. processamento assíncrono
9. regras específicas
10. testes
11. Docker
12. documentação

---

# Organização dos Cards

As tarefas foram divididas em pequenos cards para facilitar:

- rastreamento
- progresso
- commits atômicos
- PRs organizados

---

# Principais Dificuldades

## Configuração de Ambiente

Foram encontrados problemas relacionados a:

- Java
- Maven
- Docker Desktop
- IntelliJ
- portas ocupadas

---

## Kafka/Redpanda

A configuração do Redpanda exigiu atenção devido à diferença entre:

- host local
- rede interna Docker

---

## Testcontainers

A integração com Testcontainers apresentou instabilidade devido ao ambiente Docker local.

A estrutura dos testes foi criada e validada parcialmente.

---

# O Que Foi Implementado

- API REST
- persistência
- Flyway
- PostgreSQL
- Kafka/Redpanda
- consumer assíncrono
- Strategy Pattern
- regras por tipo de pedido
- idempotência
- Docker
- Makefile
- documentação

---

# O Que Melhoraria

Com mais tempo eu implementaria:

- Outbox Pattern
- DLQ
- Retry com exponential backoff
- Swagger
- observabilidade
- métricas
- tracing
- autenticação
- CI/CD
- testes mais completos

---

# Conclusão

A solução foi construída priorizando:

- clareza arquitetural
- separação de responsabilidades
- organização
- facilidade de execução
- aderência ao escopo