# Sistema de Mobilidade Urbana — Projeto Integrador Full Stack

Modernização de sistema legado de mobilidade urbana utilizando **Java 17**, **Spring Boot 3.3.4** (Spring MVC, Spring Data JPA, Spring Security), **H2 Database**, **Thymeleaf** e **Bootstrap 5**.

## Pré-requisitos
- Java JDK 17+
- Maven 3.8+ (ou Maven Wrapper `./mvnw`)

## Como Executar
1. Clone o repositório:
   ```bash
   git clone <URL_DO_SEU_REPOSITORIO>
   cd web

## Evidências de Funcionalidade
1. **Scripts SQL:** Ao iniciar a aplicação, o arquivo `data.sql` popula o banco automaticamente com Cidades e Linhas.
2. **JPA e Relacionamentos:** Entidades mapeadas com `@OneToMany` / `@ManyToOne` e chaves primárias utilizando `UUID` por segurança.
3. **Operações Persistidas (Controller & Service):**
    - Acesse `http://localhost:8080/linhas` no navegador para receber o JSON populado do banco, comprovando o fluxo ponta a ponta.
    - Acesse o painel do banco de dados em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:mobilidadedb`, User: `sa`, Password: em branco) para consultar as tabelas geradas pelo Hibernate.

