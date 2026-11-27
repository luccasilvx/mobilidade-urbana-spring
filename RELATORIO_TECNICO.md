# Relatório Técnico Final — Modernização do Sistema de Mobilidade Urbana

**Autor:** Lucas Freitas da Silva (RA: 41358)  
**Disciplina:** Programação Web — ESEG (2º Semestre de 2026)  
**Stack:** Java 17, Spring Boot 3.3.4 (MVC, Data JPA, Security, Validation), H2 Database, Thymeleaf, Bootstrap 5, JUnit 5, Mockito e MockMvc.

---

## 1. Diagnóstico do Modelo Legado vs. Modelo Relacional Proposto

### 1.1 Modelo Relacional Original (Legado — `15_mobilidade_urbana.xlsx`)
O sistema legado operava em abas desnormalizadas com chaves inteiras sequenciais, tipagem textual e redundâncias que violavam a 3ª Forma Normal (3FN):

* `Cidades(id_cidade PK, nome, estado, populacao)`
* `Linhas(id_linha PK, id_cidade FK, codigo, nome, extensao_km, ativa_texto, modal_texto)`
* `Estacoes_Pontos(id_ponto PK, id_linha FK, nome, acessivel_texto, bicicletario_texto)` — *cardinalidade 1:N equivocada, impedindo terminais compartilhados.*
* `Veiculos(id_veiculo PK, id_cidade, capacidade, ativo_texto, modal_texto)` — *redundância de `id_cidade` e tipagem fraca.*
* `Viagens(id_viagem PK, id_cidade, id_linha FK, id_veiculo FK, data_hora_inicio, status_texto)` — *violação da 3FN por repetir `id_cidade` já derivável via `Linha`, e status em texto livre.*
* `Ocorrencias(id_ocorrencia PK, id_viagem FK, tipo_texto, gravidade_texto, resolvida_texto)`
* **Segurança:** Ausência total de controle de usuários, perfis ou autenticação.

### 1.2 Modelo Relacional Proposto (Normalizado em 3FN)
* `cidade(id UUID PK, nome, estado, populacao)`
* `linha(id UUID PK, cidade_id UUID FK NOT NULL, codigo VARCHAR(20) UNIQUE NOT NULL, nome VARCHAR(120) NOT NULL, extensao_km DOUBLE NOT NULL, esta_ativa BOOLEAN NOT NULL, modal VARCHAR(30) NOT NULL)`
* `estacao_ponto(id UUID PK, nome, esta_acessivel BOOLEAN, integra_bicicletario BOOLEAN)`
* `linha_ponto(linha_id UUID FK, ponto_id UUID FK)` — *tabela associativa N:N resolvendo terminais compartilhados.*
* `veiculo(id UUID PK, capacidade, esta_ativo BOOLEAN, modal VARCHAR(30))`
* `viagem(id UUID PK, linha_id UUID FK, veiculo_id UUID FK, data_hora_inicio TIMESTAMP, status VARCHAR(30))`
* `ocorrencia(id UUID PK, viagem_id UUID FK, tipo VARCHAR(30), gravidade VARCHAR(30), resolvida BOOLEAN)`
* `usuario(id UUID PK, username, password)` / `role(id UUID PK, nome)` / `usuario_role(usuario_id UUID FK, role_id UUID FK)`

---

## 2. Índices, Restrições, Estratégia de Migração e Nota Conceitual sobre UUID

### 2.1 Índices e Restrições de Integridade
* **Índices JPA (`@Table(indexes = ...)`):** Criados `idx_linha_codigo` (único) para acelerar buscas e evitar duplicidade de códigos de linha, e `idx_linha_cidade` na chave estrangeira `cidade_id` para otimizar junções e listagens filtradas por cidade.
* **Constraints de Banco (`@Column`):** Obrigatoriedade (`nullable = false`), unicidade (`unique = true`) e limites de tamanho (`length`) aplicados nos atributos centrais, somados à integridade referencial por chaves estrangeiras (tratada no controller ao impedir exclusão de linhas com viagens/estações vinculadas).
* **Validação de Entrada (Bean Validation):** Uso de `@NotBlank`, `@NotNull`, `@Positive` e `@Size` com `@Valid` e `BindingResult` na camada MVC, retornando feedback por campo sem quebrar o fluxo.

### 2.2 Estratégia de Migração de Dados do Legado
A migração foi estruturada em quatro etapas determinísticas (materializadas na carga inicial do `src/main/resources/data.sql`):
1. **Saneamento de tipos:** Conversão de textos livres ("Sim"/"Não") para `BOOLEAN` e padronização de domínios fechados em `Enum` persistidos como `STRING` (`Modal`, `StatusViagem`, `TipoOcorrencia`, `GravidadeOcorrencia`).
2. **Mapeamento de chaves (De-Para):** Substituição dos IDs sequenciais legados por `UUID` fixos na carga inicial, preservando a integridade referencial entre entidades dependentes.
3. **Deduplicação e normalização:** Remoção de colunas transitivas (`id_cidade` em `viagem`) e extração dos vínculos `Estacoes_Pontos -> Linhas` para a tabela associativa `linha_ponto` (ex.: `Terminal Santo Amaro` vinculado simultaneamente às linhas `L01001` e `L01002`).
4. **Carga de cenários operacionais:** Inserção de múltiplas cidades, linhas ativas/inativas, veículos ativos/inativos e viagens nos 4 estados (`AGENDADA`, `EM_CURSO`, `CONCLUIDA`, `CANCELADA`) para validação integral das regras de negócio.

### 2.3 Esclarecimento Conceitual sobre UUID e Segurança
A adoção de `UUID` (`GenerationType.UUID`) evita enumeração trivial de identificadores em URLs e facilita geração distribuída de chaves sem colisão em migrações. **Contudo, `UUID` não constitui mecanismo de segurança nem substitui controle de acesso:** a prevenção real contra vulnerabilidades como IDOR e escalação de privilégio é garantida exclusivamente pela autorização no servidor (`SecurityFilterChain` no `SecurityConfig`), que valida a autenticação e o perfil (`ROLE_ADMIN` vs. `ROLE_OPERADOR`) a cada requisição HTTP.

---

## 3. Matriz de Rastreabilidade de Requisitos

| ID | Requisito (Revisado / Novo) | Implementação (Camadas) | Verificação / Evidência |
| :--- | :--- | :--- | :--- |
| **REQ-01** | Normalização 3FN, relacionamento N:N (`linha_ponto`) e Enums em string | `entities/*`, `repositories/*`, `data.sql` | `WebApplicationTests.contextLoads` + inspeção H2 |
| **REQ-02** | CRUD completo de `Linha` com validações e integridade referencial | `Linha`, `LinhaService`, `LinhaController`, `linhas/*.html` | `LinhaServiceTest` (5 testes) + `SecurityAndWebIntegrationTest` |
| **REQ-03** | Controle de transição de status de `Viagem` e bloqueio de veículo inativo | `ViagemService`, `ViagemController`, `viagens/lista.html` | `ViagemServiceTest` (4 testes) + `SecurityAndWebIntegrationTest` |
| **REQ-04** | Autenticação com BCrypt e autorização no servidor (`ADMIN` vs `OPERADOR`) | `SecurityConfig`, `login.html`, `error/403.html` | `SecurityAndWebIntegrationTest` (redirecionamento, 200 e 403) |
| **REQ-05** | Restrição do Console H2 e ausência de credenciais expostas na UI | `SecurityConfig` (`/h2-console/**` = `ADMIN`), `login.html` | `SecurityAndWebIntegrationTest` (HTTP 403 para `OPERADOR` no H2) |
| **REQ-06** | Tratamento centralizado de exceções sem exposição de stacktrace | `GlobalExceptionHandler`, `error/erro.html`, `application.properties` | `server.error.include-stacktrace=never` + fluxos de erro MVC |

---

## 4. Consolidação das Correções Aplicadas (Feedback das Entregas 1, 2 e 3)
* **Compilação e MVC:** Corrigidos imports pendentes, removido `@RestController` indevido e `@RequestMapping("/linhas")` conflitante em `LinhaController` (convertido para `@Controller` MVC com rotas 100% alinhadas ao `SecurityConfig`).
* **Segurança:** Removidas as credenciais que estavam visíveis no `login.html`; acesso ao `/h2-console/**` restrito exclusivamente a `ROLE_ADMIN`; página dedicada `error/403.html` configurada no `SecurityFilterChain`.
* **Regras de Negócio e Erros:** Adicionado Bean Validation (`@Valid` + `BindingResult`), `ViagemController` + interface `viagens/lista.html` para operar transições de status na web, tratamento de `DataIntegrityViolationException` na exclusão/duplicidade e `GlobalExceptionHandler`.
* **Banco em Testes:** Ajustado `spring.jpa.hibernate.ddl-auto=create-drop` para garantir isolamento limpo do banco H2 em memória entre múltiplos contextos de teste.

---

## 5. Evidências de Testes Automatizados

Execução integral via Maven/JUnit 5 (`./mvnw test`): **17 testes executados, 17 aprovados, 0 falhas, 0 erros** (~15s).

| Classe de Teste | Tipo | Cenários Cobertos | Resultado |
| :--- | :--- | :--- | :--- |
| `WebApplicationTests` | Contexto Spring | Subida completa da aplicação, validação JPA e carga do `data.sql` | 1/1 OK |
| `LinhaServiceTest` | Unitário (Mockito) | Cadastro válido, caso-limite (`0.01 km`), extensão zero/negativa, nula e ID inexistente | 5/5 OK |
| `ViagemServiceTest` | Unitário (Mockito) | Transição válida (`AGENDADA -> EM_CURSO`), bloqueio `CANCELADA -> CONCLUIDA`, bloqueio de pulo `AGENDADA -> CONCLUIDA` e veículo inativo | 4/4 OK |
| `SecurityAndWebIntegrationTest` | Integração (`MockMvc`) | Redirecionamento anônimo para `/login`, consulta `OPERADOR` (200), bloqueio `OPERADOR` em escrita e `/h2-console/` (403), cadastro válido `ADMIN`, erro de Bean Validation (`extensaoKm`), transição válida e bloqueio de transição inválida via POST | 7/7 OK |

---

## 6. Limitações Conhecidas, Riscos Residuais e Evolução Futura
* **Persistência em memória:** O H2 (`jdbc:h2:mem`) reinicializa os dados a cada subida; em produção deve ser substituído por PostgreSQL com migrações versionadas via Flyway.
* **Autenticação em memória:** Os perfis `admin` e `operador` utilizam `InMemoryUserDetailsManager` com hash BCrypt para reprodutibilidade imediata. As entidades `Usuario` e `Role` já estão modeladas e populadas no `data.sql` para migração direta para um `UserDetailsService` conectado ao `UsuarioRepository`.
* **Cobertura de telas:** As telas web cobrem o CRUD completo de `Linha` e a listagem/transição de status de `Viagem`; como evolução futura, adicionar telas administrativas completas para `Cidade`, `Veiculo`, `EstacaoPonto` e `Ocorrencia`.