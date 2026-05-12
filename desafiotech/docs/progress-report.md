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

Durante o desenvolvimento inicial do projeto, os commits foram realizados diretamente na branch main enquanto o ambiente, a infraestrutura e a base da aplicação estavam sendo estabilizados.

Devido ao prazo reduzido do desafio e ao tempo investido na resolução de problemas de setup e configuração, optei por priorizar a evolução contínua da aplicação e a entrega funcional do fluxo principal do sistema.

Apesar disso, mantive a preocupação com organização dos commits, utilização de conventional commits e separação lógica das funcionalidades implementadas ao longo do desenvolvimento.


---

# Ajustes de Ambiente Durante o Desenvolvimento

Durante o desenvolvimento foram encontrados alguns problemas relacionados ao ambiente local, principalmente envolvendo:

- Docker Desktop
- portas já utilizadas
- comunicação entre containers
- configuração do Redpanda/Kafka
- diferenças entre host local e rede Docker

---

# Conflito de Porta

Durante a execução local da aplicação foi identificado conflito na porta padrão:

```text
8080
```

Para evitar impacto no desenvolvimento e permitir debugging contínuo, a aplicação local passou a utilizar a porta alternativa:

```text
http://localhost:8081
```

Essa alteração foi aplicada apenas no ambiente local via IntelliJ.

A execução oficial via Docker Compose continua utilizando:

```text
http://localhost:8080
```

---

# Estratégia Adotada

Considerando o prazo reduzido do desafio, a prioridade foi manter:

- evolução contínua do sistema
- estabilidade do ambiente
- funcionamento do fluxo principal
- consistência arquitetural

Mesmo durante ajustes de infraestrutura e configuração.