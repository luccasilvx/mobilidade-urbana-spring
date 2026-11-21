---

### 4. `RELATORIO_TECNICO.md` (Raiz do Projeto)
Crie o arquivo **`RELATORIO_TECNICO.md`** na raiz do projeto (cobre integralmente o item 5.4 do edital):

```markdown
# Relatório Técnico — Modernização do Sistema de Mobilidade Urbana

**Autor:** Lucas Freitas da Silva (RA: 41358)  
**Disciplina:** Programação Web — ESEG (2º Semestre de 2026)

---

## 1. Problemas Identificados no Sistema Legado
A base legada (planilha `15_mobilidade_urbana.xlsx`) apresentava falhas estruturais e de integridade:
- **Violação da 3ª Forma Normal (3FN):** Redundância de `id_cidade` e `modal` nas tabelas `Viagens` e `Veiculos`, gerando risco de anomalias de atualização.
- **Cardinalidade incorreta:** `Estacoes_Pontos` possuía chave direta `id_linha` (1:N), impedindo que um terminal atendesse múltiplas linhas.
- **Tipagem fraca e estados livres:** Campos booleanos salvos como texto ("Sim"/"Não") e status sem domínio fechado, permitindo transições inconsistentes.
- **Ausência de segurança:** Inexistência de entidades e controle de acesso para usuários e perfis.

## 2. Soluções Adotadas e Justificativas
- **Normalização e Relacionamentos JPA:** Remoção das colunas redundantes, criação da tabela associativa `linha_ponto` (`@ManyToMany`) e definição explícita do lado proprietário das associações (`@ManyToOne` em `Linha`, `Viagem` e `Ocorrencia`).
- **Identificadores UUID:** Migração de inteiros sequenciais para `UUID`, prevenindo enumeração de IDs (IDOR) e suportando geração distribuída.
- **Domínios Fechados e Regras de Negócio:** Adoção de `Boolean` e `Enum` (`@Enumerated(EnumType.STRING)`) combinados com validações nos Services (`LinhaService` bloqueia extensão `<= 0`; `ViagemService` bloqueia transições inválidas como `CANCELADA -> CONCLUIDA` e viagens com veículo inativo).
- **Segurança no Servidor:** Spring Security com `BCryptPasswordEncoder`, login/logout e restrição de rotas no servidor (`SecurityFilterChain`) diferenciando `ROLE_ADMIN` e `ROLE_OPERADOR`, complementada por diretivas `sec:authorize` no Thymeleaf.
- **Interface Responsiva:** Templates Thymeleaf estilizados com Bootstrap 5 integrados diretamente aos Controllers MVC.

## 3. Testes Realizados e Cobertura
Foram implementados testes unitários com **JUnit 5** e **Mockito** isolando a camada de serviço:
- **`LinhaServiceTest` (5 cenários):** Cadastro válido, caso-limite de extensão mínima (`0.01 km`), rejeição de extensão zero/negativa, rejeição de extensão nula e busca de ID inexistente.
- **`ViagemServiceTest` (4 cenários):** Transição válida (`AGENDADA -> EM_CURSO`), bloqueio de transição inválida (`CANCELADA -> CONCLUIDA`), bloqueio de salto de estado (`AGENDADA -> CONCLUIDA`) e bloqueio de veículo inativo.
- **Funcionalidades não cobertas por testes automatizados:** Fluxos de renderização HTML/Thymeleaf (`@WebMvcTest`) e testes de integração de filtros do Spring Security foram validados via inspeção e fluxo manual, ficando fora da suíte unitária atual.

## 4. Limitações Conhecidas, Riscos Residuais e Melhorias Futuras
- **Persistência em memória:** O uso do H2 (`jdbc:h2:mem`) descarta dados ao reiniciar a aplicação; em produção deve ser substituído por PostgreSQL com migrações versionadas (Flyway).
- **Autenticação em memória:** Os usuários de acesso estão configurados via `InMemoryUserDetailsManager` para reprodutibilidade imediata, embora as entidades `Usuario` e `Role` já estejam mapeadas e populadas no `data.sql` para futura integração via `UserDetailsService` customizado.
- **Expansão de telas:** O CRUD web completo foi implementado para a entidade central `Linha`; como evolução futura, expandir os templates Thymeleaf para `Viagem`, `Veiculo` e `Ocorrencia`.